package model.services;

import model.entities.CarRental;
import model.entities.Invoice;

public class RentalService {

	private Double pricePerDay;
	private Double pricePerHour;
	
	private BrasilTaxService brasilTaxService;

	public RentalService(Double pricePerDay, Double pricePerHour, BrasilTaxService brasilTaxService) {
		this.pricePerDay = pricePerDay;
		this.pricePerHour = pricePerHour;
		this.brasilTaxService = brasilTaxService;
	}
	
	public void processInvoice(CarRental carRental) {
		long startDate = carRental.getStart().getTime(); // milisegundos
		long finishDate = carRental.getFinish().getTime();
		//mili -> seg/ seg -> min/ min -> hour
		double hours = (double) (finishDate - startDate) / 1000 / 60 / 60;
		
		double basicPayment;
		if(hours <= 12.0)
			basicPayment = Math.ceil(hours) * pricePerHour; // função math para arredondar o valor
		else
			basicPayment = Math.ceil(hours / 24) * pricePerDay; // hour -> day
		
		double tax = brasilTaxService.tax(basicPayment);
		carRental.setInvoice(new Invoice(basicPayment, tax));
	}
}
