 package com.tcs.microservice.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.tcs.microservice.currencyconversionservice.bean.CurrencyConversionBean;

@RestController
public class CurrencyConversionController {
	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	
	/*@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}") //where {from} and {to} represents the column   
	//return a bean back  
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)  
	{  
	return new CurrencyConversionBean(1L, from,to,BigDecimal.TEN, quantity,quantity);  
	}  */
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}") //where {from} and {to} represents the column    return a bean back  
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)  
	{  
		
		//Setting variables to currency exchange service
		Map<String, String> uriVariable = new HashMap<>();
		uriVariable.put("from",from);
		uriVariable.put("to", to);
		
		// calling the currency-exchange-service 
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate()
		.getForEntity("http://localhost:9021/currency-exchange/from/{from}/to/{to}",
		CurrencyConversionBean.class,uriVariable);
		
		CurrencyConversionBean response = responseEntity.getBody();
		
		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()));  
	}  
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}") //where {from} and {to} represents the column    return a bean back  
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)  
	{  
		CurrencyConversionBean response = proxy.retrieveExchangeValue(from, to);
		
		return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity,
				quantity.multiply(response.getConversionMultiple()));
		
	}  
	
	
}
