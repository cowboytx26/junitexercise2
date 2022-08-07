package com.udacity.cart.service;

import com.udacity.cart.model.CartItem;
import com.udacity.cart.model.CartTotals;
import com.udacity.cart.model.User;
import com.udacity.cart.model.UserType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TotalsWithDiscountCalculatorTest {

	private User testUserPlat;
	private User testUserReg;
	private User testUserRegCredit;

	private TotalsWithDiscountCalculator totalsWithDiscountCalculator;
	private TotalsWithDiscountCalculator regularCalculator;
	private TotalsWithDiscountCalculator platinumCalculator;

	@BeforeAll
	public void setUp() {
		testUserPlat = new User("Brian", UserType.PLATINUM, 0.0);
		testUserReg = new User("Test", UserType.REGULAR, 0.0);
		testUserRegCredit = new User("UserPerson", UserType.REGULAR, 100.0);
	}

	@BeforeEach
	public void init() {
		System.out.println("init executed");
		totalsWithDiscountCalculator = new TotalsWithDiscountCalculator(testUserRegCredit);
		regularCalculator = new TotalsWithDiscountCalculator(testUserReg);
		platinumCalculator = new TotalsWithDiscountCalculator(testUserPlat);
	}

	// Replace this with a Repeated test. Use a BeforeAll method to create a user whose credit
	// will be reduced with each repetition, and use a BeforeEach method to create a new TotalsWithDiscountCalculator
	// for each repetition.
	@ParameterizedTest(name = "[{index}] {0}")
	@ValueSource(doubles = {100.0, 40.0, 0.0})
	public void totalsWithDiscount_getTotals_reducesUserCredit(double expected) {

		double Started = expected;

		for(int i = 1; i < 4; i++) {
			totalsWithDiscountCalculator.getTotals(List.of(new CartItem("Twenty dollar item", 20.0, 0)));
			if (expected > 0) expected = expected - 20;
			//System.out.println("Started with: " + Started + " Expected: " + expected + " getCredit: " + testUserRegCredit.getCredit());
			assertEquals(expected, testUserRegCredit.getCredit(), 0.001);
		}
	}

	public Stream<Arguments> generateData() {
		TotalsWithDiscountCalculator regularCalculatorV2 = new TotalsWithDiscountCalculator(testUserReg);
		TotalsWithDiscountCalculator platinumCalculatorV2 = new TotalsWithDiscountCalculator(testUserPlat);
		System.out.println("generateData executed");
		return Stream.of(
			Arguments.of(10.0, 1.0, 10.0, 1.0, regularCalculatorV2),
			Arguments.of(9.0, 1.0, 10.0, 1.0, platinumCalculatorV2)
		);
	}

	// Replace this with a parameterized test that uses a MethodSource to provide
	// a stream of arguments allowing you to test both regular and platinum users with the
	// same test.
	@ParameterizedTest
	@MethodSource("generateData")
	public void totalsWithDiscounts_regularAndPlatinumUser_returnsDifferentSubtotal(double ExpectedSubTotal,
			double ExpectedTaxes, double TestSubTotal, double TestTaxes, TotalsWithDiscountCalculator TestCalculator) {

		CartTotals expectedTotal = new CartTotals(ExpectedSubTotal, ExpectedTaxes);
		CartItem item = new CartItem("Test Item", TestSubTotal, TestTaxes);
		assertEquals(expectedTotal, TestCalculator.getTotals(List.of(item)));
	}
}