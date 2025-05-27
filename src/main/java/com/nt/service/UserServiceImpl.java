package com.nt.service;

import com.nt.config.JwtProvider;
import com.nt.domain.VerificationType;
import com.nt.model.TwoFactorAuth;
import com.nt.model.User;
import com.nt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements  UserService{

    @Autowired
    private UserRepository userRepository ;

    @Override
    public User findUserProfileByJwt(String jwt) {
        String email = JwtProvider.getEmailFromToken(jwt) ;
        User user = userRepository.findByEmail(email) ;
        if(user == null){
             throw new RuntimeException("User not found");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email) ;
        if(user == null){
            throw new RuntimeException("User not found");
        }
        return user;
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> user = userRepository.findById(id) ;
        if(user.isEmpty()){
            throw new RuntimeException("User not found");
        }
        return user.get() ;
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth() ;
        twoFactorAuth.setIsEnabled(true);
        twoFactorAuth.setSendTo(verificationType);
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user) ;
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword );
        return userRepository.save(user) ;
    }
}
