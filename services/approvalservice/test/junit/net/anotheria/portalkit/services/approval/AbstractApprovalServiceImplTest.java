package net.anotheria.portalkit.services.approval;

import net.anotheria.util.IdCodeGenerator;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author dagafonov
 * 
 */
public abstract class AbstractApprovalServiceImplTest {

	private ApprovalService service;

	public void setService(ApprovalService service) {
		this.service = service;
	}

	@Test
	public void testCreate() throws Exception {
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);
		assertNotNull(t);
	}
	
	@Test
	public void testGetTicketByID() throws Exception {

		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);
		assertNotNull(t);
		
		Ticket ticket = service.getTicketById(t.getTicketId());
		assertNotNull(ticket);
	}
	
	@Test(expected=ApprovalServiceException.class)
	public void testGetTicketByIDWrongTicketId() throws Exception {

		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);
		assertNotNull(t);
		
		Ticket ticket = service.getTicketById("wrong ticket id");
		assertNotNull(ticket);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateReferenceIdIsEmpty() throws Exception {
		service.createTicket(null, 1);
	}

}
