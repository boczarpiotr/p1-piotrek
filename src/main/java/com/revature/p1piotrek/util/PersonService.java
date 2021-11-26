package com.revature.p1piotrek.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.p1piotrek.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ObjectMapper objectMapper;


    public List<Person> findAll() {
        return (List<Person>) personRepository.findAll();
    }

    public Person getPersonById(int id) {
        return personRepository.findById(id).get();
    }

    public void saveOrUpdate(Person person) {
        personRepository.save(person);
    }

    public void delete(int id) {
        personRepository.deleteById(id);
    }

    public Person savePersonFromGlobalAPI(String inputName) {
        URL url = null;
        try {
            url = new URL("https://api.nationalize.io/?name=" + inputName);
        } catch (MalformedURLException e) {
            System.err.println(e.getMessage());
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        InputStream response = null;
        try {
            response = connection.getInputStream();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        String body = null;
        try {
            body = new String(response.readAllBytes());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        Person person = new Person();
        JsonNode nameNode = null;
        try {
            nameNode = objectMapper.readTree(body).path("name");
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
        String name = nameNode.toString().replace("\"", "");
        JsonNode countryNode = null;
        try {
            countryNode = objectMapper.readTree(body).path("country");
        } catch (JsonProcessingException e) {
            System.err.println(e.getMessage());
        }
        String listOfCountries = countryNode.toString();
        person.setName(name);
        person.setCountries(listOfCountries);

        return person;
    }

}