package com.devsuperior.dsclient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsclient.entity.Client;

public interface ClientRespository extends JpaRepository<Client, Long>
{

}
