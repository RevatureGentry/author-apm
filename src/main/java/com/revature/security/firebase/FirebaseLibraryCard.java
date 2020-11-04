package com.revature.security.firebase;

import com.google.firebase.auth.FirebaseToken;
import com.revature.security.LibraryCard;

/**
 * @author William Gentry
 */
public class FirebaseLibraryCard extends LibraryCard {

  public FirebaseLibraryCard(FirebaseToken token) {
    super(token.getClaims());
  }
}
