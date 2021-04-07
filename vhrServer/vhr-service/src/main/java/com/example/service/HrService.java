package com.example.service;

import com.example.dao.HrDao;
import com.example.entity.Hr;
import com.example.entity.Role;
import com.example.utils.Utils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HrService implements UserDetailsService {
    HrDao hrDao;

    public HrService(HrDao hrDao) {
        this.hrDao = hrDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //在authenticate方法中先调用retrieveUser通过用户名获取用户和它的密码
        //再在additionalAuthenticationChecks方法中根据查询出的密码和提交的密码进行效验
        //UsernamePasswordAuthenticationFilter.attemptAuthentication
        //AbstractUserDetailsAuthenticationProvider.authenticate
        //DaoAuthenticationProvider.retrieveUser
        //DaoAuthenticationProvider.additionalAuthenticationChecks

        Hr hr = hrDao.loadUserByUsername(username);
        if (hr == null) throw new UsernameNotFoundException("用户名不存在");
        return hr;
    }

    public List<Hr> getAllHr(String username) {
        return hrDao.getAllHr(Utils.getCurrentHr().getId(), username);
    }

    public int updateEnable(Hr hr) {
        return hrDao.update(hr);
    }

    @Transactional
    public int updateHrRoles(Hr hr) {
        List<Integer> ids = hr.getRoles().stream().map(Role::getId).collect(Collectors.toList());
        int num = 1;
        hrDao.deleteRolesById(hr.getId());
        if (ids.size() != 0)
            num = hrDao.insertRolesById(hr.getId(), ids);
        return num;
    }

    @Transactional
    public Integer deleteById(Integer id) {
        hrDao.deleteRolesById(id);
        return hrDao.deleteById(id);
    }

    public Integer updateInfo(Hr hr) {
        return hrDao.updateInfo(hr);
    }

    public boolean matchOld(String old, String username, PasswordEncoder encoder) {
        String password = hrDao.loadUserByUsername(username).getPassword();
        return encoder.matches(old, password);
    }
}
