package com.rcbook;

import com.rcbook.service.user.BrandService;
import com.rcbook.service.user.CarService;
import com.rcbook.service.user.ChassisService;
import com.rcbook.service.user.UserService;
import com.rcbook.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
@RestController
public class RcbookApplication extends SpringBootServletInitializer {

	private static List<Car> carList = new ArrayList<>();
	private static List<Race> raceList = new ArrayList<>();
	private static List<Club> clubList = new ArrayList<>();

	private static AtomicLong carId = new AtomicLong(1);
	private static AtomicLong raceId = new AtomicLong(1);
	private static AtomicLong clubId = new AtomicLong(1);

	@Autowired
	private UserService userService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ChassisService chassisService;

	@Autowired
	private CarService carService;

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}

	@RequestMapping("/dashboard")
	public Map<String, Object> home() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("nbRace", raceList.size());
		model.put("nbCar", carList.size());
		return model;
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody UserCreateForm userCreateForm, HttpServletRequest request) {
		Optional<User> user = userService.getUserByEmail(userCreateForm.getEmail());
		if (!user.isPresent()) {
			userService.create(userCreateForm);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/car", method = RequestMethod.POST)
	public @ResponseBody Car addCar(@RequestBody Car car) {
		return carService.createCar(car);
	}

	@RequestMapping(value = "/race", method = RequestMethod.POST)
	public @ResponseBody Race addRace(@RequestBody Race race) {
		race.setId(raceId.getAndIncrement());
		raceList.add(race);
		Club club = getClubById(race.getRaceClub().getId());
		if (club!=null) {
			club.getRaces().add(race);
		}
		return race;
	}

	@RequestMapping(value = "/getCar", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Car> getCar() {
		return carList;
	}

	@RequestMapping(value = "/getRace", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Race> getRace() {
		return raceList;
	}

	@RequestMapping(value = "/getBrands", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Brand> getBrands() {
		return brandService.getAllBrands();
	}

	@RequestMapping(value = "/race/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Race getRace(@PathVariable String id) {
		for (Race race : raceList) {
			if (race.getId() == Long.valueOf(id)) {
				return race;
			}
		}
		return null;
	}

	@RequestMapping(value = "/race/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Void> updateRace(@RequestBody Race raceUpdated) {
		for (Race race : raceList) {
			if (race.getId() == raceUpdated.getId()) {
				raceList.remove(race);
				break;
			}
		}
		raceList.add(raceUpdated);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/club", method = RequestMethod.POST)
	public @ResponseBody Club addClub(@RequestBody Club club) {
		club.setId(clubId.getAndIncrement());
		clubList.add(club);
		userService.updateRole(club.getOwner().getId());
		return club;
	}

	@RequestMapping(value = "/club", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Club> getAllClubs() {
		return clubList;
	}

	@RequestMapping(value = "/club/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Club getClub(@PathVariable String id) {
		Club club = getClubById(Long.valueOf(id));
		if (club != null) {
			return club;
		}
		return null;
	}

	private Club getClubById(Long id) {
		for (Club club : clubList) {
			if (club.getId() == id) {
				return club;
			}
		}
		return null;
	}

	@RequestMapping(value = "/club/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Void> updateClub(@RequestBody Club clubUpdated) {
		for (Club club : clubList) {
			if (club.getId() == clubUpdated.getId()) {
				clubList.remove(club);
				break;
			}
		}
		clubList.add(clubUpdated);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/getOwnerClub", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Club getOwnerClub(@RequestParam  String userId) {
		for (Club club : clubList) {
			if (club.getOwner().getId() == Long.valueOf(userId)) {
				return club;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		SpringApplication.run(RcbookApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(applicationClass);
	}

	private static Class<RcbookApplication> applicationClass = RcbookApplication.class;


	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic().and().authorizeRequests()
					.antMatchers("/index.html", "/login.html", "/", "/signup").permitAll().anyRequest()
					.authenticated().and().csrf()
					.csrfTokenRepository(csrfTokenRepository()).and()
					.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		}

		private Filter csrfHeaderFilter() {
			return new OncePerRequestFilter() {
				@Override
				protected void doFilterInternal(HttpServletRequest request,
												HttpServletResponse response, FilterChain filterChain)
						throws ServletException, IOException {
					CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
							.getName());
					if (csrf != null) {
						Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
						String token = csrf.getToken();
						if (cookie == null || token != null
								&& !token.equals(cookie.getValue())) {
							cookie = new Cookie("XSRF-TOKEN", token);
							cookie.setPath("/");
							response.addCookie(cookie);
						}
					}
					filterChain.doFilter(request, response);
				}
			};
		}

		private CsrfTokenRepository csrfTokenRepository() {
			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			repository.setHeaderName("X-XSRF-TOKEN");
			return repository;
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth
					.userDetailsService(userDetailsService)
					.passwordEncoder(new BCryptPasswordEncoder());
		}
	}

}
