package com.example.gestaoacademica.service;

import com.example.gestaoacademica.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class ProfessorUserDetailsService implements UserDetailsService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.gestaoacademica.model.Professor professor = professorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Professor não encontrado com o username: " + username));

        return new User(professor.getUsername(), professor.getPassword(), new ArrayList<>());
    }
}