package org.example;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Iterator;
import com.novell.ldap.util.Base64;
import com.novell.ldap.LDAPSearchResults;
//import org.apache.commons.codec.binary.Base64;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import org.junit.Test;


public class TestLDAP {
  /**
   * Openldap 产生SSHA密码的算法
   * 效果等同于 slappasswd -h {ssha} -s password
   * @param password
   * @return
   * @throws NoSuchAlgorithmException
   * @throws UnsupportedEncodingException
   */

  String ldapHost = "localhost"; //ldap服务端地址
  String loginDN = "cn=Manager,dc=maxcrc,dc=com"; //ldap管理员DN
  String password = "secret"; //ldap管理员密码
  String baseGroup = "ou=Group,dc=maxcrc,dc=com";
  String base = "dc=maxcrc,dc=com"; //目录信息树（DIT）的根节点

  String user = "cn=hadoop,ou=People,dc=maxcrc,dc=com";
  public static String generateSSHAPwd(String password)
      throws NoSuchAlgorithmException, UnsupportedEncodingException {
    final int SALT_LENGTH = 4;
    SecureRandom secureRandom = new SecureRandom();
    byte[] salt = new byte[SALT_LENGTH];
    secureRandom.nextBytes(salt);

    MessageDigest crypt = MessageDigest.getInstance("SHA-1");
    crypt.reset();
    crypt.update(password.getBytes("utf-8"));
    crypt.update(salt);
    byte[] hash = crypt.digest();

    byte[] hashPlusSalt = new byte[hash.length + salt.length];
    System.arraycopy(hash, 0, hashPlusSalt, 0, hash.length);
    System.arraycopy(salt, 0, hashPlusSalt, hash.length, salt.length);

    return new StringBuilder().append("{SSHA}")
        //.append(new String(Base64.encodeBase64(hashPlusSalt), Charset.forName("utf-8")))
        .toString();
  }

  @Test
  public  void testLDAPAddEntry() throws NoSuchAlgorithmException, UnsupportedEncodingException {

    int ldapPort = LDAPConnection.DEFAULT_PORT;
    int ldapVersion = LDAPConnection.LDAP_V3;
    LDAPConnection lc = new LDAPConnection();

    //User
    LDAPAttributeSet attributeSetUser = new LDAPAttributeSet();
    attributeSetUser.add(new LDAPAttribute("objectClass", new String[]{"inetOrgPerson", "posixAccount", "top"}));
    attributeSetUser.add(new LDAPAttribute("uid", "hadoop")); //uid 是登录系统的用户名
    attributeSetUser.add(new LDAPAttribute("sn", "hadoop"));
    attributeSetUser.add(new LDAPAttribute("cn", "hadoop"));
    attributeSetUser.add(new LDAPAttribute("uidNumber", "1000"));
    attributeSetUser.add(new LDAPAttribute("loginShell", "/bin/bash"));
    attributeSetUser.add(new LDAPAttribute("homeDirectory", "/home/hadoop"));
    attributeSetUser.add(new LDAPAttribute("userPassword", generateSSHAPwd("111111")));
    attributeSetUser.add(new LDAPAttribute("gidNumber", "10000"));
    String user = "cn=hadoop,ou=People,dc=maxcrc,dc=com";
    LDAPEntry newEntryUser = new LDAPEntry(user, attributeSetUser);

    //group
    LDAPAttributeSet attributeSetGroup = new LDAPAttributeSet();
    attributeSetGroup.add(new LDAPAttribute("objectClass", new String[]{"posixGroup", "top"}));
    attributeSetGroup.add(new LDAPAttribute("cn", "hadoop"));
    attributeSetGroup.add(new LDAPAttribute("gidNumber", "10000"));
    attributeSetGroup.add(new LDAPAttribute("description", "hadoop组"));
    attributeSetGroup.add(new LDAPAttribute("memberUid", "1000"));
    String group = "cn=hadoop,ou=Group,dc=maxcrc,dc=com";
    LDAPEntry newEntryGroup = new LDAPEntry(group, attributeSetGroup);

    try {
      lc.connect(ldapHost, ldapPort);
      lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));
      System.out.println("login ldap server successfully.");

      lc.add(newEntryUser);
      System.out.println("Added object: " + user + " successfully.");
      lc.add(newEntryGroup);
      System.out.println("Added object: " + group + " successfully.");
    } catch (LDAPException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      System.out.println("Error: " + e.toString());
    } finally {
      try {
        if (lc.isConnected()) {
          lc.disconnect();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public  void testLDAPDeleteEntry () {
    //要删除节点的DN
    String deleteDN = "cn=cn,ou=People,dc=maxcrc,dc=com";

    int ldapPort = LDAPConnection.DEFAULT_PORT;
    int ldapVersion = LDAPConnection.LDAP_V3;
    LDAPConnection lc = new LDAPConnection();
    try {
      lc.connect(ldapHost, ldapPort);
      lc.bind(ldapVersion, loginDN, password.getBytes("UTF8"));

      lc.delete(deleteDN);
      System.out.println(" delete Entry: " + deleteDN + " success.");
      lc.disconnect();
    } catch (LDAPException e) {
      if (e.getResultCode() == LDAPException.NO_SUCH_OBJECT) {
        System.err.println("Error: No such object");
      } else if (e.getResultCode() == LDAPException.INSUFFICIENT_ACCESS_RIGHTS) {
        System.err.println("Error: Insufficient rights");
      } else {
        System.err.println("Error: " + e.toString());
      }
    } catch (UnsupportedEncodingException e) {
      System.out.println("Error: " + e.toString());
    } finally {
      try {
        if (lc.isConnected()) {
          lc.disconnect();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public  void testQueryEntry() {

    String searchFilter = "(&(objectClass=posixGroup)(cn=hadoop))";
    int ldapPort = LDAPConnection.DEFAULT_PORT;
    // 查询范围
    int searchScope = LDAPConnection.SCOPE_SUB;

    LDAPConnection lc = new LDAPConnection();
    try {
      lc.connect(ldapHost, ldapPort);
      lc.bind(LDAPConnection.LDAP_V3, loginDN, password.getBytes("UTF8"));
      LDAPSearchResults searchResults = lc.search(base, searchScope, searchFilter, null, false);

      while (searchResults.hasMore()) {
        LDAPEntry nextEntry = null;
        try {
          nextEntry = searchResults.next();
        } catch (LDAPException e) {
          System.out.println("Error: " + e.toString());
          if (e.getResultCode() == LDAPException.LDAP_TIMEOUT
              || e.getResultCode() == LDAPException.CONNECT_ERROR) {
            break;
          } else {
            continue;
          }
        }

        System.out.println("DN =: " + nextEntry.getDN());
        System.out.println("|---- Attributes list: ");

        LDAPAttributeSet attributeSet = nextEntry.getAttributeSet();
        Iterator<LDAPAttribute> allAttributes = attributeSet.iterator();
        while (allAttributes.hasNext()) {
          LDAPAttribute attribute = allAttributes.next();
          String attributeName = attribute.getName();

          Enumeration<String> allValues = attribute.getStringValues();
          if (null == allValues) {
            continue;
          }
          while (allValues.hasMoreElements()) {
            String value = allValues.nextElement();

            if (!Base64.isLDIFSafe(value)) {
              // base64 encode and then print out
              value = Base64.encode(value.getBytes());
            }
            System.out.println("|---- ---- " + attributeName
                + " = " + value);
          }
        }
      }

    } catch (LDAPException e) {
      System.out.println("Error: " + e.toString());
    } catch (UnsupportedEncodingException e) {
      System.out.println("Error: " + e.toString());
    } finally {
      try {
        if (lc.isConnected()) {
          lc.disconnect();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

}