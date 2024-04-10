package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dao.AccountRepository;
import ait.cohort34.accounting.dto.RolesDto;
import ait.cohort34.accounting.dto.UserDto;
import ait.cohort34.accounting.dto.UserEditDto;
import ait.cohort34.accounting.dto.UserRegisterDto;
import ait.cohort34.accounting.dto.exceptions.UserNotFoundException;
import ait.cohort34.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService{

    final AccountRepository accountRepository;
    final ModelMapper modelMapper;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        UserAccount user = modelMapper.map(userRegisterDto, UserAccount.class);
        user = accountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto removeUser(String login) {
        UserAccount user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        accountRepository.delete(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String firstName = userEditDto.getFirstName();
        if(firstName != null) {
            user.setFirstName(firstName);
        }
        String lastName = userEditDto.getLastName();
        if(lastName != null) {
            user.setLastName(lastName);
        }
        user = accountRepository.save(user);
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount user = accountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        if(isAddRole) {
            user.addRole(role.toUpperCase());
        } else {
            user.removeRole(role.toUpperCase());
        }
        user = accountRepository.save(user);
        return modelMapper.map(user, RolesDto.class);
    }

    @Override
    public void changePassword(String login, String newPassword) {

    }
}
