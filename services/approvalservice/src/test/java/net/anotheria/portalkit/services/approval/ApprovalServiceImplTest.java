package net.anotheria.portalkit.services.approval;

import net.anotheria.portalkit.services.approval.persistence.ApprovalPersistenceService;
import net.anotheria.portalkit.services.approval.persistence.TicketDO;
import net.anotheria.portalkit.services.common.AccountId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

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
        ticket.setReferenceType(1);
        ticket.setReferenceId("1");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setType(TicketType.IN_APPROVAL);
        ticket.setCreated(System.currentTimeMillis());
        ticket.setFulfillment(System.currentTimeMillis());
        ticket.setPresentation(System.currentTimeMillis());
        ticket.setAccountId(new AccountId("TEST"));

        when(persistenceService.createTicket(any(TicketDO.class))).thenReturn(ticket.toDO());

        approvalService.createTicket(ticket);

        verify(persistenceService, atLeastOnce()).createTicket(any(TicketDO.class));
    }

    @Test
    public void testApproveTicket() throws Exception {

        TicketBO ticket = new TicketBO();

        ticket.setTicketId(2);
        ticket.setLocale("EN");
        ticket.setReferenceType(1);
        ticket.setReferenceId("2");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setType(TicketType.IN_APPROVAL);
        ticket.setCreated(System.currentTimeMillis());
        ticket.setFulfillment(System.currentTimeMillis());
        ticket.setPresentation(System.currentTimeMillis());
        ticket.setAccountId(new AccountId("TEST"));

        approvalService.approveTicket(ticket);

        verify(persistenceService, atLeastOnce()).updateTicket(any(TicketDO.class));
    }

    @Test
    public void testDisapproveTicket() throws Exception {

        TicketBO ticket = new TicketBO();

        ticket.setTicketId(3);
        ticket.setLocale("EN");
        ticket.setReferenceType(1);
        ticket.setReferenceId("3");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setType(TicketType.IN_APPROVAL);
        ticket.setCreated(System.currentTimeMillis());
        ticket.setFulfillment(System.currentTimeMillis());
        ticket.setPresentation(System.currentTimeMillis());
        ticket.setAccountId(new AccountId("TEST"));

        approvalService.disapproveTicket(ticket);

        verify(persistenceService, atLeastOnce()).updateTicket(any(TicketDO.class));
    }

    @Test
    public void testGetTicketById() throws Exception {

        TicketBO result = null;
        TicketBO ticket = new TicketBO();

        ticket.setTicketId(4);
        ticket.setLocale("EN");
        ticket.setReferenceType(1);
        ticket.setReferenceId("4");
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setType(TicketType.IN_APPROVAL);
        ticket.setCreated(System.currentTimeMillis());
        ticket.setFulfillment(System.currentTimeMillis());
        ticket.setPresentation(System.currentTimeMillis());
        ticket.setAccountId(new AccountId("TEST"));

        when(persistenceService.getTicketById(4)).thenReturn(ticket.toDO());

        result = approvalService.getTicketById(4);

        verify(persistenceService, atLeastOnce()).getTicketById(4);

        assertEquals(4, result.getTicketId());
    }



    @Test
    public void testDeleteTicketsByAccountId() throws Exception {
        AccountId accountId = new AccountId("TEST");

        TicketBO ticket1 = new TicketBO();
        ticket1.setTicketId(5);
        ticket1.setLocale("EN");
        ticket1.setReferenceType(1);
        ticket1.setReferenceId("4");
        ticket1.setStatus(TicketStatus.OPEN);
        ticket1.setType(TicketType.IN_APPROVAL);
        ticket1.setCreated(System.currentTimeMillis());
        ticket1.setFulfillment(System.currentTimeMillis());
        ticket1.setPresentation(System.currentTimeMillis());
        ticket1.setAccountId(new AccountId("TEST"));

        TicketBO ticket2 = new TicketBO();
        ticket2.setTicketId(5);
        ticket2.setLocale("EN");
        ticket2.setReferenceType(1);
        ticket2.setReferenceId("4");
        ticket2.setStatus(TicketStatus.OPEN);
        ticket2.setType(TicketType.IN_APPROVAL);
        ticket2.setCreated(System.currentTimeMillis());
        ticket2.setFulfillment(System.currentTimeMillis());
        ticket2.setPresentation(System.currentTimeMillis());
        ticket2.setAccountId(new AccountId("TEST"));

        List<TicketDO> ticketDOS = Arrays.asList(ticket1.toDO(), ticket2.toDO());

        when(persistenceService.getTicketsByAccountId(accountId)).thenReturn(ticketDOS);
        doNothing().when(persistenceService).deleteTicketsByAccountId(accountId);

        approvalService.deleteTicketsByAccountId(accountId);

        verify(persistenceService, atLeastOnce()).getTicketsByAccountId(accountId);
        verify(persistenceService, atLeastOnce()).deleteTicketsByAccountId(accountId);
    }
}
