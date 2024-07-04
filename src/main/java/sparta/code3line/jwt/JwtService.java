package sparta.code3line.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j(topic = "JwtService")
@Component
public class JwtService {

    // 환경변수에서 JWT 시크릿키 가져오기~
    @Value("${jwt.key}")
    private String secret;

    // 엑세스 토큰 만료시간
    @Value("${jwt.access-expire-time}")
    private long accessExpireTime;

    // 리프레쉬 토큰 만료시간
    @Value("${jwt.refresh-expire-time}")
    private long refreshExpireTime;

    private Key key;

    public static final String BEARER_PREFIX = "Bearer ";

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 엑세스 토큰 만료시 토큰 재발급
    public String regenerateAccessToken(String refreshToken, String username) {

        if (!isValidToken(refreshToken)) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        return generateAccessToken(username);

    }

    // 토큰 디코딩
    @PostConstruct
    public void init() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);

    }

    // 엑세스 토큰
    public String generateAccessToken(String username) {

        return createToken(username, accessExpireTime);

    }

    // 리프레쉬 토큰
    public String generateRefreshToken(String username) {

        return createToken(username, refreshExpireTime);

    }

    // 토큰 생성
    private String createToken(String username, Long expirationTime) {

        Date now = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + expirationTime))
                        .signWith(key, signatureAlgorithm)
                        .compact();

    }

    // 토큰 검증
    public boolean isValidToken(String token) {

        String okToken = getToken(token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(okToken);
            return true;
        } catch (JwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;

    }

    // 에러 메시지 가져오기
    public String getErrorMessage(String token) {

        String okToken = getToken(token);
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(okToken);
            return "정상 JWT token 입니다.";
        } catch (JwtException e) {
            return "Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.";
        } catch (IllegalArgumentException e) {
            return "JWT claims is empty, 잘못된 JWT 토큰 입니다.";
        }

    }

    // 토큰 만료시간 추출
    public Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);

    }

    // 토큰에서 사용자 주체를 추출하는 메서드
    public String extractUsername(String token) {

        return extractClaim(token, Claims::getSubject);

    }

    // 토큰에서 특정 클레임을 추출하는 메서드
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    // 토큰에서 모든 클레임 추출
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(getToken(token))
                .getBody();
    }

    public Claims getClaims(String token) {

        return extractAllClaims(token);

    }

    // 토큰 가져오기
    public String getToken(String token) {

        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {

            return token.replace(BEARER_PREFIX, "");

        }

        return null;

    }
}
