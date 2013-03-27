package net.anotheria.portalkit.services.approval;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.anotheria.util.IdCodeGenerator;

import org.junit.Test;

/**
 * 
 * @author dagafonov
 * 
 */
public abstract class AbstractApprovalServiceImplTest {

	/**
	 * Destination service instance.
	 */
	private ApprovalService service;

	/**
	 * Default reservation object.
	 */
	private static final String RESERVATION_OBJECT = "r.o.";

	public void setService(ApprovalService service) {
		this.service = service;
	}

	@Test
	public void testCreateTicket() throws ApprovalServiceException {
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);
		assertNotNull(t);
	}

	@Test
	public void testGetTicketByID() throws ApprovalServiceException {
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);
		assertNotNull(t);
		Ticket ticket = service.getTicketById(t.getTicketId());
		assertNotNull(ticket);
	}

	@Test(expected = ApprovalServiceException.class)
	public void testGetTicketByIDWrongTicketId() throws ApprovalServiceException {

		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);
		assertNotNull(t);

		Ticket ticket = service.getTicketById("wrong ticket id");
		assertNotNull(ticket);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateReferenceIdIsEmpty() throws ApprovalServiceException {
		service.createTicket(null, 1);
	}

	@Test
	public void testApproveTicket() throws ApprovalServiceException {

		// creating ticket
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);

		// check status. should be in_approval
		Ticket ticket1 = service.getTicketById(t.getTicketId());
		assertEquals(TicketStatus.IN_APPROVAL, ticket1.getStatus());

		// approving the ticket
		service.approveTicket(t, RESERVATION_OBJECT);

		// check status. should be approved
		Ticket ticket2 = service.getTicketById(t.getTicketId());
		assertEquals(TicketStatus.APPROVED, ticket2.getStatus());
	}

	@Test
	public void testApproveTickets() throws ApprovalServiceException {

		// creating tickets
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t1 = service.createTicket(refId, 1);

		refId = IdCodeGenerator.generateCode(10);
		Ticket t2 = service.createTicket(refId, 1);

		service.approveTickets(Arrays.asList(new Ticket[] { t1, t2 }), RESERVATION_OBJECT);

		Ticket ticket1 = service.getTicketById(t1.getTicketId());
		Ticket ticket2 = service.getTicketById(t2.getTicketId());

		assertEquals(TicketStatus.APPROVED, ticket1.getStatus());
		assertEquals(TicketStatus.APPROVED, ticket2.getStatus());

	}

	@Test
	public void testDisapproveTicket() throws ApprovalServiceException {

		// creating ticket
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);

		// check status. should be in_approval
		Ticket ticket1 = service.getTicketById(t.getTicketId());
		assertEquals(TicketStatus.IN_APPROVAL, ticket1.getStatus());

		// approving the ticket
		service.disapproveTicket(t, RESERVATION_OBJECT);

		// check status. should be approved
		Ticket ticket2 = service.getTicketById(t.getTicketId());
		assertEquals(TicketStatus.DISAPPROVED, ticket2.getStatus());
	}

	@Test
	public void testDisapproveTickets() throws ApprovalServiceException {

		// creating tickets
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t1 = service.createTicket(refId, 1);

		refId = IdCodeGenerator.generateCode(10);
		Ticket t2 = service.createTicket(refId, 1);

		service.disapproveTickets(Arrays.asList(new Ticket[] { t1, t2 }), RESERVATION_OBJECT);

		Ticket ticket1 = service.getTicketById(t1.getTicketId());
		Ticket ticket2 = service.getTicketById(t2.getTicketId());

		assertEquals(TicketStatus.DISAPPROVED, ticket1.getStatus());
		assertEquals(TicketStatus.DISAPPROVED, ticket2.getStatus());

	}

	@Test
	public void testProceedTickets() throws ApprovalServiceException {
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t1 = service.createTicket(refId, 1);

		refId = IdCodeGenerator.generateCode(10);
		Ticket t2 = service.createTicket(refId, 1);

		refId = IdCodeGenerator.generateCode(10);
		Ticket t3 = service.createTicket(refId, 1);

		refId = IdCodeGenerator.generateCode(10);
		Ticket t4 = service.createTicket(refId, 1);

		service.proceedTickets(Arrays.asList(new Ticket[] { t1, t2 }), Arrays.asList(new Ticket[] { t3, t4 }), RESERVATION_OBJECT);

	}

	@Test
	public void testDeleteTicket() throws ApprovalServiceException {
		String refId = IdCodeGenerator.generateCode(10);
		Ticket t = service.createTicket(refId, 1);

		service.deleteTicket(t);
		try {
			Ticket ticket = service.getTicketById(t.getTicketId());
			fail("this should not be happened");
		} catch (Exception e) {
			// do nothing, be quiet!
		}
	}

	private void createTickets(long referenceType, int number) throws ApprovalServiceException {
		for (int i = 0; i < number; i++) {
			String refId = IdCodeGenerator.generateCode(10);
			service.createTicket(refId, referenceType);
		}
	}

	@Test
	public void testReserve32() throws ApprovalServiceException {
		long referenceType = 10;
		createTickets(referenceType, 32);
		Collection<Ticket> reservedTickets = service.getAndReserveTickets(RESERVATION_OBJECT, 30, referenceType);
		assertNotNull(reservedTickets);
		assertEquals(30, reservedTickets.size());

		Collection<Ticket> rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(30, rt.size());
	}

	@Test
	public void testReserve28() throws ApprovalServiceException {
		long referenceType = 11;
		createTickets(referenceType, 28);
		Collection<Ticket> reservedTickets = service.getAndReserveTickets(RESERVATION_OBJECT, 30, referenceType);
		assertNotNull(reservedTickets);
		assertEquals(28, reservedTickets.size());

		Collection<Ticket> rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(28, rt.size());
	}

	@Test
	public void testReserve122() throws ApprovalServiceException {
		long referenceType = 12;
		createTickets(referenceType, 122);
		Collection<Ticket> reservedTickets = service.getAndReserveTickets(RESERVATION_OBJECT, 120, referenceType);
		assertNotNull(reservedTickets);
		assertEquals(120, reservedTickets.size());

		Collection<Ticket> rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(120, rt.size());
	}

	@Test
	public void testReserve118() throws ApprovalServiceException {
		long referenceType = 13;
		createTickets(referenceType, 118);
		Collection<Ticket> reservedTickets = service.getAndReserveTickets(RESERVATION_OBJECT, 120, referenceType);
		assertNotNull(reservedTickets);
		assertEquals(118, reservedTickets.size());

		Collection<Ticket> rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(118, rt.size());
	}

	@Test
	public void testGetAndReserveTickets() throws ApprovalServiceException {

		long referenceType = 21;

		String refId;

		for (int i = 0; i < 200; i++) {
			refId = IdCodeGenerator.generateCode(10);
			service.createTicket(refId, referenceType);
		}

		Collection<Ticket> reservedTickets = service.getAndReserveTickets(RESERVATION_OBJECT, 30, referenceType);
		assertNotNull(reservedTickets);
		assertEquals(30, reservedTickets.size());

		Collection<Ticket> rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(30, rt.size());

		List<Ticket> reserverTickets = new ArrayList<Ticket>(rt);

		List<Ticket> toApprove = new ArrayList<Ticket>();
		reserverTickets = retrieveTickets(reserverTickets, toApprove, 10);

		service.approveTickets(toApprove, RESERVATION_OBJECT);

		rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(20, rt.size());

		List<Ticket> toDisapprove = new ArrayList<Ticket>();

		reserverTickets = retrieveTickets(reserverTickets, toDisapprove, 10);

		service.disapproveTickets(toDisapprove, RESERVATION_OBJECT);

		rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(10, rt.size());

		reservedTickets = service.getAndReserveTickets(RESERVATION_OBJECT, 47, referenceType);
		assertNotNull(reservedTickets);
		assertEquals(47, reservedTickets.size());

		rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(47, rt.size());

		reservedTickets = service.getAndReserveTickets(RESERVATION_OBJECT, 40, referenceType);
		assertNotNull(reservedTickets);
		assertEquals(40, reservedTickets.size());

		rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(40, rt.size());

		service.unreserveTickets(RESERVATION_OBJECT, referenceType);

		rt = service.getReservedTickets(RESERVATION_OBJECT, referenceType);
		assertNotNull(rt);
		assertEquals(0, rt.size());

	}

	@Test
	public void testGetAndReserveTickets2() throws ApprovalServiceException {

		long referenceType = 20;

		String refId;
		System.out.println("------------------  create  -------------------");
		for (int i = 0; i < 200; i++) {
			refId = IdCodeGenerator.generateCode(10);
			service.createTicket(refId, referenceType);
		}

		System.out.println("------------------  reserve by 3  -------------------");
		for (int i = 0; i < 30; i++) {
			List<Ticket> reservedTickets = (List<Ticket>) service.getAndReserveTickets(RESERVATION_OBJECT, 3, referenceType);
			assertNotNull(reservedTickets);
			assertEquals(3, reservedTickets.size());

			Ticket t1 = reservedTickets.get(0);
			Ticket t2 = reservedTickets.get(1);

			service.approveTicket(t1, RESERVATION_OBJECT);
			service.disapproveTicket(t2, RESERVATION_OBJECT);

		}

		System.out.println("------------------ reserve 310 ------------------------");
		Collection<Ticket> reservedTickets = service.getAndReserveTickets(RESERVATION_OBJECT, 310, referenceType);
		assertNotNull(reservedTickets);
		assertEquals(140, reservedTickets.size());

		service.approveTickets(reservedTickets, RESERVATION_OBJECT);

	}

	/**
	 * 
	 * @param rt
	 * @param result
	 * @param maxNumber
	 * @return
	 */
	private List<Ticket> retrieveTickets(List<Ticket> rt, List<Ticket> result, int maxNumber) {
		if (rt.size() < maxNumber) {
			maxNumber = rt.size();
		}

		Ticket[] src = rt.toArray(new Ticket[] {});
		Ticket[] dest = new Ticket[maxNumber];
		System.arraycopy(src, 0, dest, 0, maxNumber);

		result.addAll(Arrays.asList(dest));

		int dest2Length = rt.size() - maxNumber;
		Ticket[] dest2 = new Ticket[dest2Length];
		System.arraycopy(src, maxNumber, dest2, 0, dest2Length);

		return Arrays.asList(dest2);
	}
}
