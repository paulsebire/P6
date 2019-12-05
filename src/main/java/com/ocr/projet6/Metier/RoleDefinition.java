package com.ocr.projet6.Metier;

import com.ocr.projet6.enums.RoleEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoleDefinition {

   static public List<RoleEnum> userRole = Collections.singletonList(RoleEnum.USER);
   static public List<RoleEnum> adminRole = Arrays.asList(RoleEnum.USER, RoleEnum.ADMINISTRATOR);
}
