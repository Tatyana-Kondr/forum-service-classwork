package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dao.AccountRepository;
import ait.cohort34.accounting.dto.RolesDto;
import ait.cohort34.accounting.dto.UserDto;
import ait.cohort34.accounting.dto.UserEditDto;
import ait.cohort34.accounting.dto.UserRegisterDto;
import ait.cohort34.accounting.dto.exceptions.IncorrectRoleException;
import ait.cohort34.accounting.dto.exceptions.UserExistsException;
import ait.cohort34.accounting.dto.exceptions.UserNotFoundException;
import ait.cohort34.accounting.model.Role;
import ait.cohort34.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {

    final AccountRepository userAccountRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if (userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserExistsException();
        }
        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        String password = passwordEncoder.encode(userRegisterDto.getPassword());
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto removeUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        if (userEditDto.getFirstName() != null) {
            userAccount.setFirstName(userEditDto.getFirstName());
        }
        if (userEditDto.getLastName() != null) {
            userAccount.setLastName(userEditDto.getLastName());
        }
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        role = role.toUpperCase();
        boolean res;
        try {
            if (isAddRole) {
                res = userAccount.addRole(role);
            } else {
                res = userAccount.removeRole(role);
            }
        } catch (Exception e) {
            throw new IncorrectRoleException();
        }
        if (res) {
            userAccountRepository.save(userAccount);
        }
        return modelMapper.map(userAccount, RolesDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String password = passwordEncoder.encode(newPassword);
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userAccountRepository.existsById("admin")) {
            String password = passwordEncoder.encode("admin");
            UserAccount userAccount = new UserAccount("admin", password, "", "");
            userAccount.addRole(Role.MODERATOR.name());
            userAccount.addRole(Role.ADMIN.name());
            userAccountRepository.save(userAccount);
        }
    }
}