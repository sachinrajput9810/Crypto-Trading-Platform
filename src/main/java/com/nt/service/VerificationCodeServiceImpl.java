package com.nt.service;

import com.nt.domain.VerificationType;
import com.nt.model.User;
import com.nt.model.VerificationCode;
import com.nt.repository.VerificationCodeRepository;
import com.nt.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{

    @Autowired
    private VerificationCodeRepository verificationCodeRepository ;


    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode verificationCode = new VerificationCode() ;
        verificationCode.setUser(user);
        verificationCode.setOtp(OtpUtils.generateOTP());
        verificationCode.setVerificationType(verificationType);
        return verificationCodeRepository.save(verificationCode) ;
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id) ;
        if(verificationCode.isPresent()){
            return verificationCode.get() ;
        }
        throw new Exception("Verification Code not found") ;
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId) ;
    }

    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode) ;
    }
}
