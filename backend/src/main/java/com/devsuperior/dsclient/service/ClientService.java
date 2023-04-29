package com.devsuperior.dsclient.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dsclient.dto.ClientDTO;
import com.devsuperior.dsclient.entity.Client;
import com.devsuperior.dsclient.excepetions.DatabaseException;
import com.devsuperior.dsclient.excepetions.ResourceNotFoundException;
import com.devsuperior.dsclient.repository.ClientRespository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService
{
	
	@Autowired
	private ClientRespository repository;
	
	public List<ClientDTO> findAll()
	{
		List<Client> list = repository.findAll();
		
		return list.stream().map(c -> new ClientDTO(c)).collect(Collectors.toList());
	}

	public Page<ClientDTO> findAllPaged(PageRequest pageRequest)
	{
		
		Page<Client> list = repository.findAll(pageRequest);
		
		return list.map(c -> new ClientDTO(c));
	}

	public ClientDTO findById(Long id)
	{
		Optional<Client> opt = repository.findById(id);
		
		Client entity = opt.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO clientDTO)
	{
		Client entity = new Client();
		
		copyDtoToEntity(clientDTO, entity);
		
		entity = repository.save(entity);
		
		return new ClientDTO(entity);
	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO clientDTO)
	{
		try
		{
			Client entity = repository.getReferenceById(id);
			
			copyDtoToEntity(clientDTO, entity);
			
			entity = repository.save(entity);
			
			return new ClientDTO(entity);
		}
		catch(EntityNotFoundException e)
		{
			throw new ResourceNotFoundException("ID NOT FOUND: " + id);
		}
	}
	
	public void delete(Long id)
	{
		try
		{
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e)
		{
			throw new ResourceNotFoundException("ID NOT FOUND: " + id);
		}
		catch(DataIntegrityViolationException e)
		{
			throw new DatabaseException("INTEGRITY VIOLATION");
		}
		
	}
	
	private void copyDtoToEntity(ClientDTO dto, Client entity)
	{
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirtthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
	}


	

}
