package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    public Client get(Long id) {

        return this.clientRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Client> findAll() {

        return (List<Client>) this.clientRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, ClientDto dto) {

        Client client;

        try {
            String name = dto.getName();
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre no puede ser vacío");
            }
            if (id == null) {
                client = new Client();
            } else {
                client = this.get(id);
            }

            client.setName(name);

            this.clientRepository.save(client);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Ya existe un cliente con ese nombre");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.clientRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        }

        this.clientRepository.deleteById(id);
    }

}
