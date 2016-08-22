package com.rs.user.service;

//import java.io.IOException;
//import java.io.Serializable;
//import java.io.StringWriter;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.security.PrivateKey;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.UUID;
//
//import javax.annotation.Resource;
//import javax.mail.MessagingException;
//import javax.mail.internet.MimeMessage;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//
//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Base64;
//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang.exception.ExceptionUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.model.entity.Permission;
import com.model.entity.Role;
import com.model.entity.User;
//import org.springframework.transaction.annotation.Transactional;
//
import com.rensframework.core.service.BaseService;
import com.rs.user.dao.RoleDao;
import com.rs.user.dao.UserDao;
//import com.rs.util.RsaUtils;
//
//import freemarker.template.Configuration;
//import freemarker.template.Template;
//import freemarker.template.TemplateException;

@Service
public class UserService extends BaseService<User> {
//
	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	
	@Override
	protected PagingAndSortingRepository<User, Long> getDao() {
		return userDao;
	}
//
//	@Autowired
//	private EhCacheCacheManager ehCacheManager;
	private Cache captchaValidateIp;
//	private Cache captchaValidateAccount;
//	private Cache captchaLoginVerification;
//	private Cache shiroAuthorizationCache;
//	private Cache passwordFactor;
//	private PrivateKey passwdPrivateKey;
//	private String passwdPublicKey;
//	private String passwdXmlPublicKey;
//	private String passwdModulus;
//	private String passwdPublicExponent;
//	@Value("${random.fixed:false}")
//	private boolean randomFixed;
//	@Resource
//	private JavaMailSenderImpl mailSender;
//	public static final String MAIL_TEMPLATE_FORGOT_PASSWORD = "template/mail/forgot_password.ftl";
//	private Configuration freemarkerConf;
//
//	@Value("${mail.defaultFrom:wolongju@yeah.net}")
//	private String mailDefaultFrom;
//	public static final String MAIL_TEMPLATE_VERIFY_EMAIL = "template/mail/verify_email.ftl";
//	@Value("${shiro.loginUrl:/login/}")
//	private String loginUrl;
//
//	public void afterPropertiesSet() throws Exception {
//		this.captchaValidateIp = this.ehCacheManager
//				.getCache("captchaValidateIp");
//
//		this.captchaValidateAccount = this.ehCacheManager
//				.getCache("captchaValidateAccount");
//
//		this.captchaLoginVerification = this.ehCacheManager
//				.getCache("captchaLoginVerification");
//		this.passwordFactor = this.ehCacheManager.getCache("passwordFactor");
//
//		this.shiroAuthorizationCache = this.ehCacheManager
//				.getCache("shiroAuthorizationCache");
//	}
//
	public boolean checkPassword(User user, String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] salt = Hex.decodeHex(user.getSalt().toCharArray());
			digest.update(salt);
			byte[] hashPassword = digest.digest(password.getBytes());
			String p = Hex.encodeHexString(hashPassword);
			return user.getPassword().equals(p);
		} catch (NoSuchAlgorithmException e) {
			this.logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e);
		} catch (DecoderException e) {
			this.logger.error(ExceptionUtils.getStackTrace(e));
			throw new RuntimeException(e);
		}
	}

	public boolean checkPassword(Long id, String password) {
		User user = (User) this.userDao.findOne(id);
		return checkPassword(user, password);
	}

//	@Transactional(readOnly = false)
//	public User updatePassword(Long id, String newPassword) {
//		User user = (User) this.userDao.findOne(id);
//		user.setPlainPassword(newPassword);
//		return saveWithEntryptPassword(user);
//	}
//
//	@Transactional(readOnly = false)
//	public User updatePasswordAndResetRandom(User user, String newPassword) {
//		user.setPlainPassword(newPassword);
//		return saveWithEntryptPassword(user);
//	}
//
//	@Transactional(readOnly = false)
//	public User saveWithEntryptPassword(User u) {
//		entryptPassword(u);
//		return (User) super.save(u);
//	}
//
//	public void entryptPassword(User user) {
//		byte[] salt = new byte[8];
//		user.setSalt(Hex.encodeHexString(salt));
//		try {
//			MessageDigest digest = MessageDigest.getInstance("SHA-1");
//			if (salt != null) {
//				digest.update(salt);
//			}
//			byte[] hashPassword = digest.digest(user.getPlainPassword()
//					.getBytes());
//			user.setPassword(Hex.encodeHexString(hashPassword));
//		} catch (NoSuchAlgorithmException e) {
//			this.logger.error(ExceptionUtils.getStackTrace(e));
//			throw new RuntimeException(e);
//		}
//	}

	public User findByAccount(String account) {
		return this.userDao.findByAccount(account);
	}

	public User findByEmail(String email) {
		return this.userDao.findByEmail(email);
	}

	public User findByCellphone(String cellphone) {
		return this.userDao.findByCellphone(cellphone);
	}

	public Long count() {
		return Long.valueOf(this.userDao.count());
	}

	public Iterable<User> getAll() {
		return this.userDao.findAll();
	}

	public Set<String> getAllPermission(Long userId) {
		User u = (User) this.userDao.findOne(userId);
		Set<String> set = new HashSet<String>();
		for (Role r : u.getRoles()) {
			for (Permission p : r.getPermissions()) {
				set.add(p.getPerm());
			}
		}
		return set;
	}

	public Set<String> getAllPermission(String account) {
		User u = this.userDao.findByAccount(account);
		Set<String> set = new HashSet<String>();
		for (Role r : u.getRoles()) {
			for (Permission p : r.getPermissions()) {
				set.add(p.getPerm());
			}
		}
		return set;
	}

//	 @Transactional(readOnly=false)
//	 public void recordLoginLog(ClientInfo clientInfo, String account, Boolean
//	 pass)
//	 {
//	 recordLoginLog(clientInfo, account, pass, null);
//	 }

//	// @Transactional(readOnly=false)
//	// public void recordLoginLog(ClientInfo clientInfo, String account, Boolean
//	// pass, String extraText)
//	// {
//	// OperationLog log = new OperationLog(clientInfo);
//	// log.setType(OperationLog.LogType.Login);
//	// String text = "������: " + account + "������";
//	// if (pass != null) {
//	// text = text + ", ���������������" + (pass.booleanValue() ? "������" :
//	// "������");
//	// }
//	// if (extraText != null) {
//	// text = text + ", " + extraText;
//	// }
//	// log.setDesc(text);
//	// log.setResult((pass != null) && (pass.booleanValue()) && (extraText ==
//	// null) ? OperationLog.Result.Success : OperationLog.Result.Failure);
//	//
//	// log.setTarget(new User().getEntityName());
//	// log.setTargetName(account);
//	// this.operationLogDao.save(log);
//	// }
//
//	// @Transactional(readOnly=false)
//	// public void recordLoginFailureLog(ClientInfo clientInfo, String account)
//	// {
//	// String ip = clientInfo.getIp();
//	// LoginFailureLog log = new LoginFailureLog();
//	// log.setIp(ip);
//	// log.setAccount(account);
//	// this.loginFailureLogDao.save(log);
//	//
//	// int loginFailureCheckHours =
//	// ((ReloadableConfig)ReloadableConfig.HOLDER.getConfig()).getLoginFailureCheckHours();
//	//
//	// int loginFailureLimit =
//	// ((ReloadableConfig)ReloadableConfig.HOLDER.getConfig()).getLoginFailureLimit();
//	// Timestamp start = new Timestamp(System.currentTimeMillis() - 3600000 *
//	// loginFailureCheckHours);
//	//
//	// long count = this.loginFailureLogDao.countByIp(ip, start);
//	// if (count >= loginFailureLimit) {
//	// this.captchaValidateIp.put(ip, Long.valueOf(count));
//	// }
//	// count = this.loginFailureLogDao.countByAccount(account, start);
//	// if (count >= loginFailureLimit) {
//	// this.captchaValidateAccount.put(account, Long.valueOf(count));
//	// }
//	// }
//
//	public void saveCaptchaLoginVerification(Serializable sessionId, String text) {
//		this.captchaLoginVerification.put(sessionId, text);
//	}
//
//	public void clearCaptchaLoginVerification(Serializable sessionId) {
//		this.captchaLoginVerification.evict(sessionId);
//	}
//
//	public boolean validateCaptchaLoginVerification(Serializable sessionId,
//			String text) {
//		if ((sessionId == null) || (text == null)) {
//			return false;
//		}
//		Cache.ValueWrapper vw = this.captchaLoginVerification.get(sessionId);
//		if (vw == null) {
//			return false;
//		}
//		boolean flag = text.equalsIgnoreCase((String) vw.get());
//		this.captchaLoginVerification.evict(sessionId);
//		return flag;
//	}

	public long captchaValidateByIp(String ip) {
		Cache.ValueWrapper e = this.captchaValidateIp.get(ip);
		return e == null ? 0L : ((Long) e.get()).longValue();
	}

//	public long captchaValidateByAccount(String account) {
//		if (StringUtils.isBlank(account)) {
//			return 0L;
//		}
//		Cache.ValueWrapper e = this.captchaValidateAccount.get(account);
//		return e == null ? 0L : ((Long) e.get()).longValue();
//	}
//
//	public Map<String, Object> checkSsoTicket(String account, String ticket) {
//		Map<String, Object> m = new HashMap<String, Object>();
//		if ((ticket == null) || (account == null)) {
//			m.put("success", Boolean.valueOf(false));
//			m.put("msg", "account或者ticket为空,account=" + account + ", ticket="
//					+ ticket);
//
//			return m;
//		}
//		User u = this.userDao.findByAccount(account);
//		if (u == null) {
//			m.put("success", Boolean.valueOf(false));
//			m.put("msg", "没有account=" + account + "对应用户");
//			return m;
//		}
//
//		m.put("success", Boolean.valueOf(false));
//		m.put("msg", "ticket=" + ticket + "验证失败");
//		return m;
//	}
//
////	public Set<String> getAllPermissiFromCache(String account) {
////		LoginInfo shiroUser = new LoginInfo();
////		shiroUser.setAccount(account);
////		shiroUser.setRealAccount(account);
////		SimplePrincipalCollection principals = new SimplePrincipalCollection(
////				shiroUser, "FORM_AUTHENTICATION");
////
////		Cache.ValueWrapper vw = this.shiroAuthorizationCache.get(principals);
////		if (vw != null) {
////			return (Set) vw.get();
////		}
////		return getAllPermission(account);
////	}
//
//	// public List<SessionRecord> getLoginRecord(long userId)
//	// {
//	// return this.sessionRecordDao.findByUserId(userId);
//	// }
//
////	@Value("${passwd.privateKey:MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCDobu5fGYdEiLMJT0/r+0wMst6zktuX7AdI3lULO6Z3/yQFcTBvVRBjBd5DEC1tcuGu1+xkPL6pZzf9KL86OGc7mZDsk5YccLin6hvkabAhk1wFlyCJPINZw9lQPSwSfBJznAPxu4qvhsJKANFhM5fHOTCr7YCDsZO5ak96B/k5bLhk34FrVDpmBX4L6lRJU+P6etQsg5qYum3L5hAN3QHOAYnAvHjXuOHPtnvJchdV8stvDC8g6RJYdr/A6bY56CpGv2FPlN4T3nryLTtVeC56y5MJg0Oi+ZUiVe2cVGddHuz6ZPJPd8hVej+G89Lv0pk1lGnCEZDpDUJLURaWAXhAgMBAAECggEAIx5qZFFxOP5WtFqXHb/FK1/R9ADIFTmIVi3ZuACI5BrjWlnEdeAac6Z5uLzxIsnO9DJXlJtUiFTZJtMxDQLg9qqQNC2FQ4mOckIggSu97o2maWo94icTQUomzF/pKIOBjV97fGoZruOreOTpAootOSkPS3XvvFrOw8v4PGEK6VmRL56OB5NyPIoYntpsSkUqiLZ0zVpxVymU57GFD6vmeudpxd3HnqF8Lem0cqLjg9nF3iSshPQiAcKoePi8rIZ+tA8hlgd+lQjDBLA1mYkQxowMngl7kog0Uk9VC11Uf+InsEI4+pen38H1B8+GAtJ/u6jAjcm7T4+9Mb1TJbK8cQKBgQDAt/MAuZS4b1jCQqhGi5e5xCcB/VGc/lWPf9oRcfw4EUKxuJFOlTNVa+Zbl+1YpIF69SHjn6vRryyVwHbnnwV0ri0y+y5R+pl0GNAR0byH8naT4LOuThfiWHlzTPksfkMdqXQ2Tj25nlI6azekNKMwdv8/tU/0t5muaWAOs51D1QKBgQCu2snMFEbzwB/j4YZYHhEM9jiEGYpscys74+6aZIInUdU6AAbw4fTBlES1ROoEL9JDC5pG4Yl2xlshBxJO+AK3NYop3YQHGwKcFE+spJYTx/wxNRUzIrpHy3VomLIy9F9kAiTDJrANgkjogxmcbBCSEmCTjd0FuyQQReXDrzkb3QKBgHbTtSC4TMvf0/GLmihNQBNJr+eZIy5S37yoT3Q8PRDxC5d7PxUeB9Xevt2w8qaaYD/JT8kDFbUndq0rS+WK9pk4ICR0Hd5sTxv3hKvID6AfTx/lB8Us36svdEs0pdraS1XJux8U5RhDV14v59H2prxmUwIcMJ2qazbQQGswMoK1AoGAMOZO+RvSm+hnArvXbl06N33EYG0kISA0PxOHbvYc0BI6p3XKeyM1580nSepz56fYDZf5FUmNwAHJCnuHyz3gxIvcj8i7W6FaIXwsrAN7VRzgkoJd42ca4Wks9It/inB9HOLclRcEtUJyUVQYnX47RB6Pk7Rg03F6rHJfx1IBjqECgYBzdCdZ8kubTsF4LLoHWMqp0ye+wun7/avNZyAkwKep8FPCiKVe0KnU0lodT4+NBL4XdGaiGya0cyn01JtnZ2dQZQu6e2lstcgk+v7GN3vs1Wuadt01dHgr8cLPg9QKYWazqnduFmXwNaWCLVsdk1MSPLlMAj090ek7a1L1mM4zMw==}")
////	public void setPasswdPrivateKey(String key) {
////		this.passwdPrivateKey = RsaUtils.toPrivateKey(key);
////	}
//
////	@Value("${passwd.publicKey:MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg6G7uXxmHRIizCU9P6/tMDLLes5Lbl+wHSN5VCzumd/8kBXEwb1UQYwXeQxAtbXLhrtfsZDy+qWc3/Si/OjhnO5mQ7JOWHHC4p+ob5GmwIZNcBZcgiTyDWcPZUD0sEnwSc5wD8buKr4bCSgDRYTOXxzkwq+2Ag7GTuWpPegf5OWy4ZN+Ba1Q6ZgV+C+pUSVPj+nrULIOamLpty+YQDd0BzgGJwLx417jhz7Z7yXIXVfLLbwwvIOkSWHa/wOm2OegqRr9hT5TeE9568i07VXguesuTCYNDovmVIlXtnFRnXR7s+mTyT3fIVXo/hvPS79KZNZRpwhGQ6Q1CS1EWlgF4QIDAQAB}")
////	public void setPasswdPublicKey(String passwdPublicKey) {
////		this.passwdPublicKey = passwdPublicKey;
////		RSAPublicKey pk = (RSAPublicKey) RsaUtils.toPublicKey(passwdPublicKey);
////		this.passwdModulus = pk.getModulus().toString(16);
////		this.passwdPublicExponent = pk.getPublicExponent().toString(16);
////		this.passwdXmlPublicKey = RsaUtils.fromXmlPublicKey(pk);
////	}
//
//	public Map<String, String> passwordPublicFactor(Serializable sessionId) {
//		String str = null;
//		Cache.ValueWrapper vw = this.passwordFactor.get(sessionId);
//		if (vw == null) {
//			if (this.randomFixed) {
//				str = ".wP7YpW.SaNhy08rltwFIKEa_qMx_66cSlZeSHBeT0UuVXf50s";
//			} else {
//				byte[] salt = new byte[38];
//			}
//			this.passwordFactor.put(sessionId, str);
//		} else {
//			str = (String) vw.get();
//		}
//		Map<String, String> map = new HashMap(5);
//		map.put("random", str);
//		map.put("n", this.passwdModulus);
//		map.put("e", this.passwdPublicExponent);
//		map.put("key", this.passwdPublicKey);
//		map.put("xmlKey", this.passwdXmlPublicKey);
//		return map;
//	}
//
////	public String[] decryptPasswordString(Serializable sessionId,
////			String... dataArr) {
////		if (sessionId == null) {
////			return null;
////		}
////		Cache.ValueWrapper vw = this.passwordFactor.get(sessionId);
////		if (vw == null) {
////			return null;
////		}
////		String fullFactor = (String) vw.get();
////		this.passwordFactor.evict(sessionId);
////		String[] res = new String[dataArr.length];
////		for (int j = 0; j < dataArr.length; j++) {
////			String data = dataArr[j];
////			if (data == null) {
////				res[j] = null;
////			} else {
////				try {
////					byte[] plainData = RsaUtils.decrypt(
////							Base64.decodeBase64(data), this.passwdPrivateKey);
////
////					String plainStr = new String(plainData, "UTF-8");
////					StringBuilder factor = new StringBuilder(50);
////					StringBuilder password = new StringBuilder(50);
////					for (int i = 0; i < plainStr.length(); i++) {
////						if (i % 2 == 0) {
////							factor.append(plainStr.charAt(i));
////						} else {
////							password.append(plainStr.charAt(i));
////						}
////					}
////					if (!fullFactor.startsWith(factor.toString())) {
////						this.logger
////								.warn("session id: {}������������������������������������:{}���������������:{}",
////										new Object[] { sessionId.toString(),
////												fullFactor, factor });
////						res[j] = null;
////					} else {
////						res[j] = password.toString();
////					}
////				} catch (Exception e) {
////					this.logger.warn("������������������", e);
////					res[j] = null;
////				}
////			}
////		}
////		return res;
////	}
//
////	public UserService() {
////		this.freemarkerConf = new Configuration(Configuration.VERSION_2_3_22);
////
////		this.freemarkerConf.setTemplateLoader(new ClassTemplateLoader(""));
////		this.freemarkerConf.setLocalizedLookup(false);
////	}
//
//	@Transactional(readOnly = false)
//	public void forgotPassword(User u, String setPwdUrl) {
//		String random = UUID.randomUUID().toString().replace("-", "");
//		if (random.length() > 10) {
//			random = random.substring(0, 10);
//		}
//		this.userDao.save(u);
//		MimeMessage message = this.mailSender.createMimeMessage();
//
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//		Map<String, Object> root = new HashMap();
//		try {
//			root.put("url", setPwdUrl + "?username=" +
//
//			URLEncoder.encode(u.getAccount(), "UTF-8"));
//		} catch (UnsupportedEncodingException e1) {
//		}
//		StringWriter writer = new StringWriter();
//		try {
//			Template template = null;
//			if (new ClassPathResource("template/mail/forgot_password.ftl")
//					.exists()) {
//				template = this.freemarkerConf
//						.getTemplate("/template/mail/forgot_password.ftl");
//			} else {
//				template = this.freemarkerConf
//						.getTemplate("/default_template/mail/forgot_password.ftl");
//			}
//			template.process(root, writer);
//			helper.setText(writer.toString(), true);
//			helper.setSubject("密码找回");
//			helper.setTo(u.getEmail());
//			helper.setFrom(this.mailDefaultFrom);
//			this.mailSender.send(message);
//		} catch (TemplateException | IOException | MessagingException e) {
//			this.logger.error(ExceptionUtils.getStackTrace(e));
//			throw new RuntimeException(e);
//		} finally {
//			IOUtils.closeQuietly(writer);
//		}
//	}
//
//	@Transactional(readOnly = false)
//	public void sendEmailVerify(User u, String verifyEmailUrl) {
//		if (u.isEmailVerified()) {
//			return;
//		}
//		String random = UUID.randomUUID().toString().replace("-", "");
//		if (random.length() > 10) {
//			random = random.substring(0, 10);
//		}
////		u.setRandom(random);
////		u.setSendTime(new Timestamp(System.currentTimeMillis()));
//		this.userDao.save(u);
//		MimeMessage message = this.mailSender.createMimeMessage();
//
//		MimeMessageHelper helper = new MimeMessageHelper(message);
//		Map<String, Object> root = new HashMap();
//		try {
//			root.put("url", verifyEmailUrl + "?username=" +
//
//			URLEncoder.encode(u.getAccount(), "UTF-8") + "&email=" +
//
//			URLEncoder.encode(u.getEmail(), "UTF-8"));
//		} catch (UnsupportedEncodingException e1) {
//		}
//		StringWriter writer = new StringWriter();
//		try {
//			Template template = null;
//			if (new ClassPathResource("template/mail/verify_email.ftl")
//					.exists()) {
//				template = this.freemarkerConf
//						.getTemplate("/template/mail/verify_email.ftl");
//			} else {
//				template = this.freemarkerConf
//						.getTemplate("/default_template/mail/verify_email.ftl");
//			}
//			template.process(root, writer);
//			helper.setText(writer.toString(), true);
//			helper.setSubject("验证邮箱");
//			helper.setTo(u.getEmail());
//			helper.setFrom(this.mailDefaultFrom);
//			this.mailSender.send(message);
//		} catch (TemplateException | IOException | MessagingException e) {
//			this.logger.error(ExceptionUtils.getStackTrace(e));
//			throw new RuntimeException(e);
//		} finally {
//			IOUtils.closeQuietly(writer);
//		}
//	}
//
//	public Page<User> search(String account, String name,
//			Boolean removed, Pageable pageable) {
//		return this.userDao.findAll(
//				searchSpecification(account, name, removed), pageable);
//	}
//
//	@SuppressWarnings("unchecked")
//	private Specification<User> searchSpecification(final String account, final String name, final Boolean removed) {
//		return new Specification() {
//			@Override
//			public Predicate toPredicate(Root root, CriteriaQuery query,
//					CriteriaBuilder builder) {
//				List<Predicate> list = new ArrayList();
//				if (removed != null) {
//					list.add(builder.equal(root.get("removed")
//							.as(Integer.class), Integer.valueOf(removed
//							.booleanValue() ? 1 : 0)));
//				}
//				if (StringUtils.isNotBlank(account)) {
//					list.add(builder.like(root.get("account").as(String.class),
//							"%" + account + "%"));
//				}
//				if (StringUtils.isNotBlank(name)) {
//					list.add(builder.like(root.get("name").as(String.class),
//							"%" + name + "%"));
//				}
//				Predicate[] p = new Predicate[list.size()];
//				return builder.and((Predicate[]) list.toArray(p));
//			}
//		};
//	}
//
//	@Transactional(readOnly = false)
//	public void createUser(User user) {
//		int level;
//		Iterator<Role> it;
//		if ((user.getRolesForPage() != null)
//				&& (user.getRolesForPage().size() > 0)) {
//			user.setRoles(new HashSet(user.getRolesForPage().size()));
//			List<Long> longList = new ArrayList(user.getRolesForPage().size());
//			for (String str : user.getRolesForPage()) {
//				longList.add(Long.valueOf(Long.parseLong(str)));
//			}
//			Object ri = this.roleDao.findAll(longList);
////			level = user.getPool().getLevel();
//			for (it = ((Iterable) ri).iterator(); it.hasNext();) {
//				Role r = (Role) it.next();
////				if (r.getLevel() > level) {
////					throw new RuntimeException(
////							"没有使用该角色的权限");
////				}
//				user.getRoles().add(r);
//			}
//		}
//		entryptPassword(user);
//		user.setAudited(true);
//		saveWithAudit(user);
//	}
//
//	@Transactional(readOnly = false)
//	public void updateUser(User user) {
//		if (StringUtils.isNotBlank(user.getPlainPassword())) {
//			entryptPassword(user);
//		}
//		List<String> nl = user.getRolesForPage();
//		if (nl == null) {
//			nl = new ArrayList(0);
//		}
//		user.loadRolesForPage();
//		List<String> ol = user.getRolesForPage();
//
//		Map<String, Role> roleMap = new HashMap(user.getRoles().size());
//		for (Role r : user.getRoles()) {
//			roleMap.put(r.getIdStr(), r);
//		}
//		for (String l : ol) {
//			if (!nl.contains(l)) {
//				user.getRoles().remove(roleMap.get(l));
//			}
//		}
////		int level = user.getPool().getLevel();
//		for (String l : nl) {
//			if (!ol.contains(l)) {
//				Role r = (Role) this.roleDao.findOne(Long.valueOf(Long
//						.parseLong(l)));
////				if (r.getLevel() > level) {
////					throw new RuntimeException(
////							"没有使用该角色的权限");
////				}
//				user.getRoles().add(r);
//			}
//		}
//		saveWithAudit(user);
//	}
//
//	public String[] decryptPasswordString(Serializable sessionId, String... dataArr)
//	  {
//	    if (sessionId == null) {
//	      return null;
//	    }
//	    Cache.ValueWrapper vw = this.passwordFactor.get(sessionId);
//	    if (vw == null) {
//	      return null;
//	    }
//	    String fullFactor = (String)vw.get();
//	    this.passwordFactor.evict(sessionId);
//	    String[] res = new String[dataArr.length];
//	    for (int j = 0; j < dataArr.length; j++)
//	    {
//	      String data = dataArr[j];
//	      if (data == null) {
//	        res[j] = null;
//	      } else {
//	        try
//	        {
//	          byte[] plainData = RsaUtils.decrypt(Base64.decodeBase64(data), this.passwdPrivateKey);
//	          
//	          String plainStr = new String(plainData, "UTF-8");
//	          StringBuilder factor = new StringBuilder(50);
//	          StringBuilder password = new StringBuilder(50);
//	          for (int i = 0; i < plainStr.length(); i++) {
//	            if (i % 2 == 0) {
//	              factor.append(plainStr.charAt(i));
//	            } else {
//	              password.append(plainStr.charAt(i));
//	            }
//	          }
//	          if (!fullFactor.startsWith(factor.toString()))
//	          {
//	            this.logger.warn("session id: {}的密码因子错误,完整因子:{},错误因子:{}", new Object[] {sessionId
//	              .toString(), fullFactor, factor });
//	            res[j] = null;
//	          }
//	          else
//	          {
//	            res[j] = password.toString();
//	          }
//	        }
//	        catch (Exception e)
//	        {
//	          this.logger.warn("������������������", e);
//	          res[j] = null;
//	        }
//	      }
//	    }
//	    return res;
//	  }
//	
//	public String getLoginUrl() {
//		return this.loginUrl;
//	}
//
}
