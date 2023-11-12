package com.tttm.birdfarmshop.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.birdfarmshop.Constant.ConstantAPI;
import com.tttm.birdfarmshop.Constant.ConstantParametter;
import com.tttm.birdfarmshop.DTO.BirdDTO;
import com.tttm.birdfarmshop.Exception.CustomException;
import com.tttm.birdfarmshop.Models.Bird;
import com.tttm.birdfarmshop.Service.BirdService;
import com.tttm.birdfarmshop.Utils.Request.BirdMatching;
import com.tttm.birdfarmshop.Utils.Request.BirdMatchingRequest;
import com.tttm.birdfarmshop.Utils.Request.BirdRequest;
import com.tttm.birdfarmshop.Utils.Request.FilterProduct;
import com.tttm.birdfarmshop.Utils.Response.BirdMatchingResponse;
import com.tttm.birdfarmshop.Utils.Response.BirdResponse;
import com.tttm.birdfarmshop.Utils.Response.MessageResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ConstantAPI.BIRD)
public class BirdController {
    private final BirdService birdService;

    @PostMapping(ConstantAPI.ADD_BIRD)
    public ResponseEntity<MessageResponse> addBird(@RequestBody BirdDTO dto) throws CustomException {
        try {
            return new ResponseEntity<>(birdService.AddNewBird(dto), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(ConstantAPI.ADD_BIRD_LIST)
    public ResponseEntity<MessageResponse> addBird(@RequestBody List<BirdDTO> list) throws CustomException {
        try {
            MessageResponse ms = new MessageResponse();
            for (BirdDTO dto: list) {
                if(ms.getMessage() != null) {
                    ms.setMessage(ms.getMessage() + "\n" + birdService.AddNewBird(dto).getMessage());
                } else {
                    ms.setMessage(birdService.AddNewBird(dto).getMessage());
                }
            }
            return new ResponseEntity<>(ms, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(ConstantAPI.UPDATE_BIRD_OWNER)
    public ResponseEntity<BirdResponse> updateBirdOwner(@RequestParam("BirdID") String BirdID,
                                                        @RequestParam("UserID") int UserID) throws CustomException {
        try {
            return new ResponseEntity<>(birdService.updateBirdOwner(BirdID, UserID), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new BirdResponse(), HttpStatus.FORBIDDEN);
        }
    }
    @PutMapping(ConstantAPI.UPDATE_BIRD + ConstantParametter.BIRD_ID)
    public ResponseEntity<MessageResponse> updateBird(@PathVariable("BirdID") String BirdID, @RequestBody BirdDTO dto) throws CustomException {
        try {
            return new ResponseEntity<>(birdService.UpdateBird(BirdID, dto), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new MessageResponse(ex.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(ConstantAPI.GET_BIRD_BY_ID + ConstantParametter.BIRD_ID)
    public ResponseEntity<BirdResponse> getBirdByID(@PathVariable ("BirdID") String BirdID) throws CustomException {
        try {
            return new ResponseEntity<>(birdService.findBirdByBirdID(BirdID), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.GET_ALL_BIRD)
    public ResponseEntity<List<BirdResponse>> getAllBird() throws CustomException {
        try {
            return new ResponseEntity<>(birdService.findAllBird(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ConstantAPI.MATCHING_BIRD_FROM_SAME_OWNER)
    public ObjectNode matchingSameOwner(@RequestBody BirdMatchingRequest bird)
        throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BirdMatchingResponse birdChild = birdService.matchingSameOwner(bird.getFirstBird(), bird.getSecondBird());
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Matching Bird Successfully!");
            respon.put("successRate", String.valueOf(birdChild.getSuccessRate()));
            respon.set("data", objectMapper.createObjectNode()
                    .put("age", 0)
                    .put("gender", birdChild.getBird().getGender())
                    .put("ownerID", 0)
                    .put("fertility", true)
                    .put("breedingTimes", 0)
                    .put("color", birdChild.getBird().getColor().name())
                    .put("typeOfBird", birdChild.getBird().getTypeOfBird().getTypeName())
            );
            return respon;

        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }
    @PostMapping(ConstantAPI.MATCHING_BIRD_FROM_DIFFERENT_OWNER)
    public ResponseEntity<List<BirdResponse>> matchingDifferentOwner(@RequestBody BirdMatching bird)
            throws CustomException {
        try {
            return new ResponseEntity<>(birdService.matchingBirdDifferentOwner(bird), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(ConstantAPI.MATCHING_BIRD_IN_SHOP)
    public ResponseEntity<List<BirdResponse>> matchingBirdInShop(@PathParam("id") String id)
            throws CustomException {
        try {
            return new ResponseEntity<>(birdService.matchingBirdInShop(id), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(ConstantAPI.GET_BIRD_BY_NAME + ConstantParametter.BIRD_NAME)
    public ResponseEntity<List<BirdResponse>> findBirdByName(@PathVariable("BirdName") String BirdName) throws CustomException {
        try {
            return new ResponseEntity<>(birdService.findBirdByName(BirdName), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.SORT_BIRD_BY_PRICE_ASC)
    public ResponseEntity<List<BirdResponse>> sortBirdByPriceAscending() throws CustomException {
        try {
            return new ResponseEntity<>(birdService.sortBirdByPriceAscending(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.SORT_BIRD_BY_PRICE_DESC)
    public ResponseEntity<List<BirdResponse>> sortBirdByPriceDescending() throws CustomException {
        try {
            return new ResponseEntity<>(birdService.sortBirdByPriceDescending(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.SORT_BIRD_BY_ALPHABET_ASC)
    public ResponseEntity<List<BirdResponse>> sortBirdByAlphabetAscending() throws CustomException {
        try {
            return new ResponseEntity<>(birdService.sortBirdByProductNameAscending(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.SORT_BIRD_BY_ALPHABET_DESC)
    public ResponseEntity<List<BirdResponse>> sortBirdByAlphabetDescending() throws CustomException {
        try {
            return new ResponseEntity<>(birdService.sortBirdByProductNameDescending(), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(ConstantAPI.FILTER_BIRD_BY_REQUEST)
    public ResponseEntity<List<BirdResponse>> filterBirdByRequest(@RequestBody FilterProduct filterProduct) throws CustomException {
        try {
            return new ResponseEntity<>(birdService.filterBirdByCustomerRequest(filterProduct), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
