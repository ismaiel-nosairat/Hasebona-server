/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.ismaiel.hasebona.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ismaiel.hasebona.tools.JwtClient;
import io.ismaiel.hasebona.tools.JwtClientsFactory;
import io.ismaiel.hasebona.tools.SymmetricAlgo;

/**
 * @author Ismaiel Nosairat <ismaiel.nosairat@gmail.com>
 */
public class GV {
    public static JwtClient symClient = JwtClientsFactory.buildSym()
            .setKeySeed("asdfadsfdasf".getBytes())
            .setSecreteKeyAlgorithmName("AES")
            .setSignatureAlgorithm(SymmetricAlgo.HS256)
            .setTtlMillis(-1)
            .build();

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
}
