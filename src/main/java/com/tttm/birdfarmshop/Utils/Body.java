package com.tttm.birdfarmshop.Utils;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Body {
  private int orderCode;
  private int amount;
  private String description;
  private List<ObjectNode> items;
  private String cancelUrl;
  private String returnUrl;
  private String signature;
  public Body(int orderCode, int amount, String description, List<ObjectNode> items) {
    this.orderCode = orderCode;
    this.amount = amount;
    this.description = description;
    this.items = items;
  }

  public Body(int orderCode, int amount, String description, List<ObjectNode> items, String cancelUrl, String returnUrl, String signature) {
    this.orderCode = orderCode;
    this.amount = amount;
    this.description = description;
    this.items = items;
    this.cancelUrl = cancelUrl;
    this.returnUrl = returnUrl;
    this.signature = signature;
  }

}
