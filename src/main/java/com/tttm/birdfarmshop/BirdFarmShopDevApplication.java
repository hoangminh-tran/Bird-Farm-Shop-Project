package com.tttm.birdfarmshop;

import com.tttm.birdfarmshop.Enums.AccountStatus;
import com.tttm.birdfarmshop.Enums.BirdColor;
import com.tttm.birdfarmshop.Enums.ERole;
import com.tttm.birdfarmshop.Models.Bird;
import com.tttm.birdfarmshop.Models.TypeOfBird;
import com.tttm.birdfarmshop.Models.User;
import com.tttm.birdfarmshop.Repository.*;
import com.tttm.birdfarmshop.Service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;

@SpringBootApplication
public class BirdFarmShopDevApplication {

	public static void main(String[] args) {
		SpringApplication.run(BirdFarmShopDevApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository,
											   PasswordEncoder passwordEncoder,
											   HealthcareProfessionalService healthcareProfessionalService,
											   AdminService adminService,
											   SellerService sellerService,
											   ShipperService shipperService,
											   CustomerService customerService,
											   TypeOfBirdRepository typeOfBirdRepository) {
		return args -> {
			if (userRepository.getAllUsers().size() == 0) {
				var admin = User.builder()
						.firstName("admin1")
						.lastName("admin1")
						.email("admin1@gmail.com")
						.phone("123456764321")
						.password(passwordEncoder.encode("admin"))
						.gender(false)
						.dateOfBirth(Date.valueOf("2020-02-02"))
						.address("HCM")
						.accountStatus(AccountStatus.ACTIVE)
						.role(ERole.ADMINISTRATOR)
						.build();

				var healthcare = User.builder()
						.firstName("healthcare1")
						.lastName("healthcare1")
						.email("healthcare1@gmail.com")
						.phone("123456761")
						.password(passwordEncoder.encode("healthcare"))
						.gender(false)
						.dateOfBirth(Date.valueOf("2020-02-02"))
						.address("HCM")
						.accountStatus(AccountStatus.ACTIVE)
						.role(ERole.HEALTHCAREPROFESSIONAL)
						.build();

				var seller = User.builder()
						.firstName("seller1")
						.lastName("seller1")
						.email("seller1@gmail.com")
						.phone("129956764321")
						.password(passwordEncoder.encode("seller"))
						.gender(false)
						.dateOfBirth(Date.valueOf("2020-02-02"))
						.address("HCM")
						.accountStatus(AccountStatus.ACTIVE)
						.role(ERole.SELLER)
						.build();

				var shipper = User.builder()
						.firstName("shipper1")
						.lastName("shipper1")
						.email("shipper1@gmail.com")
						.phone("8298283222")
						.password(passwordEncoder.encode("shipper"))
						.gender(false)
						.dateOfBirth(Date.valueOf("2020-02-02"))
						.address("HCM")
						.accountStatus(AccountStatus.ACTIVE)
						.role(ERole.SHIPPER)
						.build();

				var customer = User.builder()
						.firstName("customer1")
						.lastName("customer1")
						.email("customer1@gmail.com")
						.phone("90908080")
						.password(passwordEncoder.encode("customer"))
						.gender(false)
						.dateOfBirth(Date.valueOf("2020-02-02"))
						.address("HCM")
						.accountStatus(AccountStatus.ACTIVE)
						.role(ERole.CUSTOMER)
						.build();

				userRepository.save(admin);
				userRepository.save(healthcare);
				userRepository.save(seller);
				userRepository.save(shipper);
				userRepository.save(customer);

				adminService.createAdmin(admin);
				healthcareProfessionalService.createHealthcareProfessional(healthcare);
				sellerService.createSeller(seller);
				shipperService.createShipper(shipper);
				customerService.createCustomer(customer);
			}
		};
	}
}
//
//			if(typeOfBirdRepository.findAll().size() == 0)
//			{
//				//------------------------------------
//				var type = TypeOfBird.builder()
//						.typeID("TB001")
//						.typeName("Chim Se")
//						.quantity(0)
//						.build();
//				typeOfBirdRepository.save(type);
//
//				type = TypeOfBird.builder()
//						.typeID("TB002")
//						.typeName("Chim Vet")
//						.quantity(0)
//						.build();
//				typeOfBirdRepository.save(type);
//
//				type = TypeOfBird.builder()
//						.typeID("TB003")
//						.typeName("Chim Bo Cau")
//						.quantity(0)
//						.build();
//				typeOfBirdRepository.save(type);
//
//				type = TypeOfBird.builder()
//						.typeID("TB004")
//						.typeName("Chim Chich Choe")
//						.quantity(0)
//						.build();
//
//				typeOfBirdRepository.save(type);
//				type = TypeOfBird.builder()
//						.typeID("TB005")
//						.typeName("Chim Chao Mao")
//						.quantity(0)
//						.build();
//
//				typeOfBirdRepository.save(type);
//				type = TypeOfBird.builder()
//						.typeID("TB006")
//						.typeName("Chim Cu Dat")
//						.quantity(0)
//						.build();
//				typeOfBirdRepository.save(type);
//			}
//		};
//	}
//}
