/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ismaiel.hasebona.exceptions.BadInputException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
public abstract class BusinessObject implements Serializable {
    @JsonIgnore
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @JsonIgnore
    private Set<ConstraintViolation<BusinessObject>> errors;

    /* Validation methods */
    public final boolean valid() {
        preValidate();
        errors = new HashSet<ConstraintViolation<BusinessObject>>();
        errors = validator.validate(this);
        postValidate();
        return errors.isEmpty();
    }

    /**
     * Method to be overwritten in subclasses so any BO can make some
     * arrangement before checking valid
     */
    protected void preValidate() {

    }

    /**
     * Method to be overwritten in subclasses so any BO can make some
     * arrangement once validation has been made
     */
    protected void postValidate() {

    }

    public Set<ConstraintViolation<BusinessObject>> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    public void validate() {
        if (!valid())
            throw new BadInputException("102", null);
    }

}
