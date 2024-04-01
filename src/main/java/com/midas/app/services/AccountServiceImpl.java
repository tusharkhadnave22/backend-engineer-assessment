package com.midas.app.services;

import com.midas.app.models.Account;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.workflows.CreateAccountWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Workflow;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Component
public class AccountServiceImpl implements AccountService {
  private final Logger logger = Workflow.getLogger(AccountServiceImpl.class);
  @Autowired private final WorkflowClient workflowClient;
  @Autowired private final AccountRepository accountRepository;

  /**
   * createAccount creates a new account in the system or provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public Account createAccount(Account details) {
    var options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(CreateAccountWorkflow.QUEUE_NAME)
            .setWorkflowId(details.getEmail())
            .build();

    logger.info("initiating workflow to create account for email: {}", details.getEmail());

    var workflow = workflowClient.newWorkflowStub(CreateAccountWorkflow.class, options);

    return workflow.createAccount(details);
  }

  /**
   * getAccounts returns a list of accounts.
   *
   * @return List<Account>
   */
  @Override
  public List<Account> getAccounts() {
    return accountRepository.findAll();
  }

  @Override
  public Account updateAccountDetails(UUID id, Account updatedAccount) {
    Account existingAccount = accountRepository.findById(id).orElse(null);

    if (existingAccount == null) {
      throw new RuntimeException("Account not found");
    }

    if (updatedAccount.getFirstName() != null) {
      existingAccount.setFirstName(updatedAccount.getFirstName());
    }
    if (updatedAccount.getLastName() != null) {
      existingAccount.setLastName(updatedAccount.getLastName());
    }
    if (updatedAccount.getEmail() != null) {
      existingAccount.setEmail(updatedAccount.getEmail());
    }

    accountRepository.save(existingAccount);
    return accountRepository.findById(id).get();
  }
}
