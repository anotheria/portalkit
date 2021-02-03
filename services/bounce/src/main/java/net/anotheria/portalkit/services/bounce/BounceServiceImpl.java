package net.anotheria.portalkit.services.bounce;

import net.anotheria.moskito.aop.annotation.Monitor;
import net.anotheria.portalkit.services.bounce.persistence.BounceDO;
import net.anotheria.portalkit.services.bounce.persistence.BouncePersistenceService;
import net.anotheria.portalkit.services.bounce.persistence.BouncePersistenceServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vlad Lukjanenko
 */
@Service
@Monitor(subsystem = "mail", category = "portalkit-service")
public class BounceServiceImpl implements BounceService {

    @Autowired
    private BouncePersistenceService bouncePersistenceService;

    
    @Override
    public void saveBounce(BounceBO bounce) throws BounceServiceException {
        try {
            bouncePersistenceService.saveBounce(bounce.toDO());
        } catch (BouncePersistenceServiceException e) {
            throw new BounceServiceException("Unable to save bounce", e);
        }
    }

    @Override
    public void deleteBounce(String email) throws BounceServiceException {
        try {
            bouncePersistenceService.deleteBounce(email);
        } catch (BouncePersistenceServiceException e) {
            throw new BounceServiceException("Unable to remove bounce, email=" + email, e);
        }
    }

    @Override
    public BounceBO getBounce(String email) throws BounceServiceException {
        try {
            return new BounceBO(bouncePersistenceService.getBounce(email));
        } catch (BouncePersistenceServiceException e) {
            throw new BounceServiceException("Unable to get bounce, email=" + email, e);
        }
    }

    @Override
    public List<BounceBO> getBounces() throws BounceServiceException {

        List<BounceDO> bounces = null;
        List<BounceBO> result = new ArrayList<>();

        try {
            bounces = bouncePersistenceService.getBounces();
        } catch (BouncePersistenceServiceException e) {
            throw new BounceServiceException("Unable to get all bounces", e);
        }

        for (BounceDO bounce : bounces) {
            result.add(new BounceBO(bounce));
        }

        return result;
    }

    @Override
    public Map<String, BounceBO> getBouncesMap() throws BounceServiceException {

        Map<String, BounceBO> bounceMap = new HashMap<>();
        List<BounceBO> bounces = getBounces();

        for (BounceBO bounce : bounces) {
            bounceMap.put(bounce.getEmail(), bounce);
        }

        return bounceMap;
    }
}
