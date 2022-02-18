package com.batuhangoktas.reviewledger.controller;

import com.batuhangoktas.reviewledger.response.BaseResponse;
import com.batuhangoktas.reviewledger.response.ReviewListResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import com.fasterxml.jackson.annotation.JsonFormat;


@RestController
public class UserCtrl {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    @RequestMapping("/register")
    public String login() {
        return register();
    }


    @CrossOrigin
    @RequestMapping("/login")
    public BaseResponse login(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        BaseResponse response = new BaseResponse();
        if (username.equals("User1") || username.equals("User2") || username.equals("User3") || username.equals("User4") || username.equals("User5")) {
            if (password.equals("123456")) {
                response.setStatus(true);
                String dateTimeStr = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
            } else
                response.setStatus(false);
            return response;
        }
        response.setStatus(false);
        return response;
    }


    @RequestMapping("/addasset")
    public String addasset(@RequestParam(name = "keyid") String keyId) {

        try (Gateway gateway = connect()) {

            System.out.println("\n");
            System.out.println("connect");
            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("basic");

            System.out.println("Submit Transaction: CreateAsset" + keyId);
            //CreateAsset creates an asset with ID asset13, color yellow, owner Tom, size 5 and appraisedValue of 1300
            contract.submitTransaction("CreateAsset", keyId, "1234", "05.05.2021", "Onaylayınız");

            System.out.println("\n");


            return "OK";
        } catch (Exception e) {
            System.err.println(e);
            return "ERROR";
        }

    }


    @RequestMapping("/ledger")
    public String ledgerasset() {

        try (Gateway gateway = connect()) {

            System.out.println("\n");
            System.out.println("connect");
            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("basic");

            System.out.println("Submit Transaction: InitLedger");
            //CreateAsset creates an asset with ID asset13, color yellow, owner Tom, size 5 and appraisedValue of 1300
            contract.submitTransaction("InitLedger");

            System.out.println("\n");


            return "OK";
        } catch (Exception e) {
            System.err.println(e);
            return "ERROR";
        }

    }

    @CrossOrigin
    @RequestMapping("/getassets")
    public ReviewListResponse getreviewlist() {
        ReviewListResponse dataResponse = new ReviewListResponse();
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("basic");

            byte[] result;
            System.out.println("\n");
            result = contract.evaluateTransaction("GetAllAssets");
            String resultStr = new String(result);
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String keyId = (object.getString("keyId"));
                result = contract.evaluateTransaction("GetReviewList", keyId);
                String getStatusStr = new String(result);
                JSONArray statusArr = new JSONArray(getStatusStr);
                if (statusArr.length() != 0) {
                    array.getJSONObject(i).put("isreview", "yes");
                } else {
                    array.getJSONObject(i).put("isreview", "no");
                }
            }
            System.out.println("GetAllAssets: result: " + array.toString());
            // System.out.println("GetAllAssets: result: " + new String(result));

            dataResponse.setStatus(true);
            dataResponse.setListReceiveData(array.toString());
            return dataResponse;
        } catch (Exception e) {
            System.err.println(e);
            dataResponse.setStatus(false);
            return dataResponse;
        }

    }

    @CrossOrigin
    @RequestMapping("/addwith")
    public BaseResponse addwith(@RequestParam(name = "keyid") String keyId, @RequestParam(name = "userid") String userId, @RequestParam(name = "dateTime") String dateTime) {
        BaseResponse response = new BaseResponse();
        try (Gateway gateway = connect()) {

            System.out.println("\n");
            System.out.println("connect");
            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("basic");

            System.out.println("Submit Transaction: ReviewSubmit" + keyId);
            //CreateAsset creates an asset with ID asset13, color yellow, owner Tom, size 5 and appraisedValue of 1300
            contract.submitTransaction("ReviewSubmit", keyId, userId, dateTime, "Onaylandı");

            System.out.println("\n");


            response.setStatus(true);
            return response;
        } catch (Exception e) {
            System.err.println(e);
            response.setStatus(false);
            return response;
        }

    }

    @CrossOrigin
    @RequestMapping("/getwith")
    public ReviewListResponse getwithlist(@RequestParam(name = "keyid") String keyId) {
        ReviewListResponse dataResponse = new ReviewListResponse();
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("basic");

            byte[] result;
            System.out.println("\n");
            result = contract.evaluateTransaction("GetReviewList", keyId);
            System.out.println("GetReviewList: result: " + new String(result));
            dataResponse.setStatus(true);
            dataResponse.setListReceiveData(new String(result));
            return dataResponse;
        } catch (Exception e) {
            System.err.println(e);
            dataResponse.setStatus(false);
            return dataResponse;
        }

    }

    public static Gateway connect() throws Exception {

        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }

    public static String register() {
        try {
            EnrollAdmin.main(null);
            RegisterUser.main(null);
            return "OK";
        } catch (Exception e) {
            System.err.println(e);
            return "ERROR";
        }
    }
}
