package com.rcbook;

import com.rcbook.service.user.*;
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

	@Autowired
	private UserService userService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private ChassisService chassisService;

	@Autowired
	private CarService carService;

	@Autowired
	private ClubService clubService;

	@Autowired
	private RaceService raceService;

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}

	@RequestMapping("/dashboard")
	public Map<String, Object> home(@RequestParam String userId) {
		Optional<User> user = userService.getUserById(Long.valueOf(userId));
		Map<String, Object> model = new HashMap<>();
		model.put("nbRace", raceService.countRaces());
		model.put("nbCar", carService.countCarByUser(user.get()));
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
		return raceService.createRace(race);
	}

	@RequestMapping(value = "/getChassis", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Chassis> getChassisByBrand(@RequestParam String brandId) {
		Brand brand = brandService.getBrandById(Long.valueOf(brandId));
		return chassisService.getChassisByBrand(brand);
	}

	@RequestMapping(value = "/getCarByUserId", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Car> getCar(@RequestParam String userId) {
		Optional<User> user = userService.getUserById(Long.valueOf(userId));
		return carService.getAllCarByUser(user.get());
	}

	@RequestMapping(value = "/getRacesByClub", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Race> getRacesbyClub(@RequestParam String clubId) {
		Club club = clubService.getClubById(Long.valueOf(clubId));
		return raceService.getRacesByClub(club);
	}

	@RequestMapping(value = "/getBrands", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Brand> getBrands() {
		return brandService.getAllBrands();
	}

	@RequestMapping(value = "/race/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Race getRace(@PathVariable String id) {
		return raceService.getRaceById(Long.valueOf(id));
	}

	@RequestMapping(value = "/race/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Void> updateRace(@RequestBody Race raceUpdated) {
		raceService.createRace(raceUpdated);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/club", method = RequestMethod.POST)
	public @ResponseBody Club addClub(@RequestBody Club club) {
		Club createdClub = clubService.createClub(club);
		userService.updateRole(club.getOwner().getId());
		return createdClub;
	}

	@RequestMapping(value = "/club", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<Club> getAllClubs() {
		return clubService.getAllClubs();
	}

	@RequestMapping(value = "/club/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Club getClub(@PathVariable String id) {
		Club club = clubService.getClubById(Long.valueOf(id));
		return club;
	}

	@RequestMapping(value = "/club/{id}", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<Void> updateClub(@RequestBody Club clubUpdated) {
		clubService.createClub(clubUpdated);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/getOwnerClub", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody Club getOwnerClub(@RequestParam String userId) {
		Optional<User> user = userService.getUserById(Long.valueOf(userId));
		return clubService.getClubByUser(user.get());
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
