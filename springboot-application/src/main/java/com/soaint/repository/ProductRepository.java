package com.soaint.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.soaint.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    @Autowired
    private DynamoDBMapper dynamoDBMapper;


    public Product save(Product employee) {
        dynamoDBMapper.save(employee);
        return employee;
    }

    public Product getEmployeeById(String employeeId) {
        return dynamoDBMapper.load(Product.class, employeeId);
    }

    public String delete(String employeeId) {
        Product emp = dynamoDBMapper.load(Product.class, employeeId);
        dynamoDBMapper.delete(emp);
        return "Employee Deleted!";
    }

    public String update(String employeeId, Product employee) {
        dynamoDBMapper.save(employee,
                new DynamoDBSaveExpression()
        .withExpectedEntry("employeeId",
                new ExpectedAttributeValue(
                        new AttributeValue().withS(employeeId)
                )));
        return employeeId;
    }
}
