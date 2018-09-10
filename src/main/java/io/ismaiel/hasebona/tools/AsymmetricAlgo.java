/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.tools;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
public enum AsymmetricAlgo {
    RS256(SignatureAlgorithm.RS256),;

    private SignatureAlgorithm algo;

    private AsymmetricAlgo(SignatureAlgorithm algo) {
        this.algo = algo;
    }

    public SignatureAlgorithm getAlgo() {
        return algo;
    }
}
