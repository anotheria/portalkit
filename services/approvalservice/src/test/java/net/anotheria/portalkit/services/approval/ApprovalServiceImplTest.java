package net.anotheria.portalkit.services.approval;

import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.TicketDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * 
 * @author Vlad Lukjanenko
 * 
 */
@RunWith(MockitoJUnitRunner.class)
public class ApprovalServiceImplTest {

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    @Mock
    private ApprovalPersistenceService persistenceService;


    @Test
    public void testCreateTicket() throws Exception {

        TicketBO ticket = new TicketBO();

        ticket.setTicketId(1);
        ticket.setLocale("EN");
        ticket.setReferenceType(ReferenceType.MESSAGE);
        ticket.setReferenceId("1");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setType(TicketType.IN_APPROVAL);
        ticket.setCreated(System.currentTimeMillis());
        ticket.setFulfillment(System.currentTimeMillis());
        ticket.setPresentation(System.currentTimeMillis());

        when(persistenceService.createTicket(any(TicketDO.class))).thenReturn(ticket.toDO());

        approvalService.createTicket(ticket);

        verify(persistenceService, atLeastOnce()).createTicket(any(TicketDO.class));
    }

    @Test
    public void testApproveTicket() throws Exception {

        TicketBO ticket = new TicketBO();

        ticket.setTicketId(2);
        ticket.setLocale("EN");
        ticket.setReferenceType(ReferenceType.MESSAGE);
        ticket.setReferenceId("2");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setType(TicketType.IN_APPROVAL);
        ticket.setCreated(System.currentTimeMillis());
        ticket.setFulfillment(System.currentTimeMillis());
        ticket.setPresentation(System.currentTimeMillis());

        approvalService.approveTicket(ticket);

        verify(persistenceService, atLeastOnce()).updateTicket(any(TicketDO.class));
    }

    @Test
    public void testDisapproveTicket() throws Exception {

        TicketBO ticket = new TicketBO();

        ticket.setTicketId(3);
        ticket.setLocale("EN");
        ticket.setReferenceType(ReferenceType.MESSAGE);
        ticket.setReferenceId("3");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setType(TicketType.IN_APPROVAL);
        ticket.setCreated(System.currentTimeMillis());
        ticket.setFulfillment(System.currentTimeMillis());
        ticket.setPresentation(System.currentTimeMillis());

        approvalService.disapproveTicket(ticket);

        verify(persistenceService, atLeastOnce()).updateTicket(any(TicketDO.class));
    }

    @Test
    public void testGetTicketById() throws Exception {

        TicketBO result = null;
        TicketBO ticket = new TicketBO();

        ticket.setTicketId(4);
        ticket.setLocale("EN");
        ticket.setReferenceType(ReferenceType.MESSAGE);
        ticket.setReferenceId("4");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setType(TicketType.IN_APPROVAL);
        ticket.setCreated(System.currentTimeMillis());
        ticket.setFulfillment(System.currentTimeMillis());
        ticket.setPresentation(System.currentTimeMillis());

        when(persistenceService.getTicketById(4)).thenReturn(ticket.toDO());

        result = approvalService.getTicketById(4);

        verify(persistenceService, atLeastOnce()).getTicketById(4);

        assertEquals(4, result.getTicketId());
    }
}
