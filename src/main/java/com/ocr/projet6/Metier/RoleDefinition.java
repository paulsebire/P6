package com.ocr.projet6.Metier;

import com.ocr.projet6.enums.RoleEnum;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RoleDefinition {

   static public List<RoleEnum> userRole = Collections.singletonList(RoleEnum.ROLE_USER);
   static public List<RoleEnum> adminRole = Arrays.asList(RoleEnum.ROLE_USER, RoleEnum.ROLE_ADMIN);
}
