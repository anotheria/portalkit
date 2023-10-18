package net.anotheria.portalkit.services.bounce;

import net.anotheria.portalkit.services.bounce.persistence.BounceDO;
import net.anotheria.portalkit.services.bounce.persistence.BouncePersistenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Vlad Lukjanenko
 */
@RunWith(MockitoJUnitRunner.class)
public class BounceServiceTest {

    @InjectMocks
    private BounceServiceImpl bounceService;

    @Mock
    private BouncePersistenceService bouncePersistenceService;


    @Test
    public void testSaveBounce() throws Exception {

        BounceBO bounce = getBounce();

        bounceService.saveBounce(bounce);

        verify(bouncePersistenceService, atLeastOnce()).saveBounce(bounce.toDO());
    }

    @Test
    public void testDeleteBounce() throws Exception {

        bounceService.deleteBounce("email@anotheria.net");

        verify(bouncePersistenceService, atLeastOnce()).deleteBounce("email@anotheria.net");
    }

    @Test
    public void testGetBounce() throws Exception {

        when(bouncePersistenceService.getBounce("email@anotheria.net")).thenReturn(getBounce().toDO());

        BounceBO bounce = bounceService.getBounce("email@anotheria.net");

        verify(bouncePersistenceService, atLeastOnce()).getBounce("email@anotheria.net");

        assertThat(bounce.getEmail(), is("email@anotheria.net"));
        assertThat(bounce.getErrorCode(), is(500));
        assertThat(bounce.getErrorMessage(), is("Error"));
    }

    @Test
    public void testGetBounces() throws Exception {



        when(bouncePersistenceService.getBounces()).thenReturn(Arrays.asList(new BounceDO[] {
                getBounce().toDO()
        }));

        List<BounceBO> bounces = bounceService.getBounces();

        verify(bouncePersistenceService, atLeastOnce()).getBounces();

        assertThat(bounces.size(), is(1));

        BounceBO bounce = bounces.get(0);

        assertThat(bounce.getEmail(), is("email@anotheria.net"));
        assertThat(bounce.getErrorCode(), is(500));
        assertThat(bounce.getErrorMessage(), is("Error"));
    }


    private BounceBO getBounce() {

        BounceBO bounce = new BounceBO();

        bounce.setEmail("email@anotheria.net");
        bounce.setErrorCode(500);
        bounce.setErrorMessage("Error");

        return bounce;
    }
}
