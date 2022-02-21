package net.anotheria.portalkit.services.approval;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.portalkit.services.common.AccountId;
import org.distributeme.annotation.DistributeMe;
import org.distributeme.annotation.FailBy;
import org.distributeme.core.failing.RetryCallOnce;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Approval service interface.
 * 
 * @author dagafonov
 * 
 */
@DistributeMe(
		initcode = {
				"net.anotheria.portalkit.services.approval.ApprovingServiceSpringConfigurator.configure();"
		}
)
@FailBy(strategyClass=RetryCallOnce.class)
public interface ApprovalService extends Service {

	/**
	 * Creates a ticket.
	 * 
	 * @param ticket	new ticket
	 * @return {@link TicketBO} instance
	 * @throws ApprovalServiceException if error.
	 */
	TicketBO createTicket(TicketBO ticket) throws ApprovalServiceException;

	/**
	 * Removes ticket.
	 * 
	 * @param referenceId ticket id.
	 * @throws ApprovalServiceException if error.
	 */
	void deleteTicketByReferenceId(String referenceId) throws ApprovalServiceException;

	/**
	 * Removes ticket.
	 *
	 * @param ticketId ticket id.
	 * @throws ApprovalServiceException if error.
	 */
	void deleteTicket(long ticketId) throws ApprovalServiceException;

	/**
	 * Removes all tickets for given {@link AccountId}.
	 *
	 * @param accountId		{@link AccountId} owner of tickets
	 * @throws ApprovalServiceException	if any errors
	 */
	void deleteTicketsByAccountId(AccountId accountId) throws ApprovalServiceException;

	/**
	 * Gets ticket by internal ID.
	 * 
	 * @param ticketId ticket id
	 * @return {@link TicketBO} instance
	 * @throws ApprovalServiceException if error
	 */
	TicketBO getTicketById(long ticketId) throws ApprovalServiceException;

	/**
	 * Gets ticket by locale with lock for given agent id.
	 *
	 * @param locale locale
	 * @param agentId agent id
	 * @return list of {@link TicketBO} instances
	 * @throws ApprovalServiceException if error
	 */
	List<TicketBO> getTicketsByLocale(String locale, String agentId) throws ApprovalServiceException;

	/**
	 * Gets ticket by type with lock for given agent id.
	 *
	 * @param referenceType		reference type
	 * @param ticketType		ticket type
	 * @param agentId 			agent id
	 * @return list of {@link TicketBO} instances
	 * @throws ApprovalServiceException if error
	 */
	List<TicketBO> getTicketsByType(TicketType ticketType, IReferenceType referenceType, String agentId) throws ApprovalServiceException;

	/**
	 * Gets ticket by type with lock for given agent id.
	 *
	 * @param referenceType		reference type
	 * @param ticketType		ticket type
	 * @param agentId 			agent id
	 * @param limit 			limit of tickets
	 * @return list of {@link TicketBO} instances
	 * @throws ApprovalServiceException if error
	 */
	List<TicketBO> getTicketsByType(TicketType ticketType, IReferenceType referenceType, String agentId, int limit) throws ApprovalServiceException;

	/**
	 * Get count of tickets according to ticket and reference types.
	 *
	 * @param ticketType		reference type
	 * @param referenceType		ticket type
	 * @return					count of corresponding tickets
	 * @throws ApprovalServiceException	if error
	 */
	long getTicketsCount(TicketType ticketType, IReferenceType referenceType) throws ApprovalServiceException;
	/**
	 * Gets ticket by type and locale with lock for given agent id.
	 *
	 * @param ticketType		reference type
	 * @param referenceType		ticket type
	 * @param locale			locale
	 * @param agentId 			agent id
	 * @return					list of {@link TicketBO} instances
	 * @throws ApprovalServiceException if any errors occurs
	 */
	List<TicketBO> getTicketsByTypeAndLocale(TicketType ticketType, IReferenceType referenceType, String locale, String agentId) throws ApprovalServiceException;

	/**
	 * Gets ticket by type.
	 *
	 * @param ticketType		ticket type
	 * @param referenceType		reference type
	 * @return list of {@link TicketBO} instances
	 * @throws ApprovalServiceException if error.
	 */
	List<TicketBO> getTicketsByType(TicketType ticketType, IReferenceType referenceType) throws ApprovalServiceException;
	/**
	 * Approves ticket.
	 *
	 * @param ticket {@link TicketBO} instance
	 * @throws ApprovalServiceException if error.
	 */
	void approveTicket(TicketBO ticket) throws ApprovalServiceException;

	/**
	 * Approve list of tickets.
	 *
	 * @param tickets tickets
	 * @throws ApprovalServiceException if error.
	 */
	void approveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException;

	/**
	 * Disapprove ticket.
	 *
	 * @param ticket	ticket
	 * @throws ApprovalServiceException if error.
	 */
	void disapproveTicket(TicketBO ticket) throws ApprovalServiceException;

	/**
	 * Disapprove tickets.
	 *
	 * @param tickets tickets
	 * @throws ApprovalServiceException if error.
	 */
	void disapproveTickets(Collection<TicketBO> tickets) throws ApprovalServiceException;

	/**
	 * Gets all locked tickets.
	 *
	 * @return list of {@link TicketBO} instances.
	 * @throws ApprovalServiceException if any errors
	 * */
	Set<TicketBO> getLockedTickets() throws ApprovalServiceException;

	/**
	 * Gets locked tickets for agent.
	 *
	 * @param locale locale
	 * @return list of {@link TicketBO} instances
	 * @throws ApprovalServiceException if any errors
	 * */
	List<TicketBO> getLockedTickets(String locale) throws ApprovalServiceException;
}
