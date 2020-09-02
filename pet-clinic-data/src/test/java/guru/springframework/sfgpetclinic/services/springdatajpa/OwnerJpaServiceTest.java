package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.repositories.OwnerRepository;
import guru.springframework.sfgpetclinic.repositories.PetRepository;
import guru.springframework.sfgpetclinic.repositories.PetTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OwnerJpaServiceTest {

    public static final String LAST_NAME = "Smith";
    @Mock
    OwnerRepository ownerRepository;

    @InjectMocks
    OwnerJpaService ownerJpaService;

    Owner returnOwner;

    @BeforeEach
    void setUp() {
        returnOwner = Owner.builder().id(1L).lastName(LAST_NAME).build();
    }

    @Test
    void findByLastName() {

        Owner returnOwner = Owner.builder().id(1L).lastName(LAST_NAME).build();

        when(ownerRepository.findByLastName(any())).thenReturn(returnOwner);

        Owner smith = ownerJpaService.findByLastName(LAST_NAME);

        assertEquals(LAST_NAME, smith.getLastName());

    }

    @Test
    void findAll() {
        Set<Owner> returnOwners = new HashSet<>();
        returnOwners.add(Owner.builder().id(1L).build());
        returnOwners.add(Owner.builder().id(2l).build());

        when(ownerRepository.findAll()).thenReturn(returnOwners);

        Set<Owner> owners = ownerJpaService.findAll();

        assertNotNull(owners);
        assertEquals(0, owners.size());
    }

    @Test
    void findById() {

        when(ownerRepository.findById(anyLong())).thenReturn(Optional.of(returnOwner));

        Owner owner = ownerJpaService.findById(1L);

        assertNotNull(owner);
    }

    @Test
    void findByIdNotFound() {

        when(ownerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Owner owner = ownerJpaService.findById(1L);

        assertNull(owner);
    }

    @Test
    void save() {

        when(ownerRepository.save(any())).thenReturn(returnOwner);

        Owner owner = ownerJpaService.save(Owner.builder().id(1L).build());

        assertNotNull(owner);

    }

    @Test
    void delete() {
        ownerJpaService.delete(returnOwner);
        verify(ownerRepository).delete(any());
    }

    @Test
    void deleteById() {
        ownerJpaService.deleteById(1L);
        verify(ownerRepository).deleteById(anyLong());
    }
}