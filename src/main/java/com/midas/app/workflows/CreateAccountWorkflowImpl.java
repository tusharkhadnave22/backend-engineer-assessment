package com.midas.app.workflows;

import com.midas.app.activities.AccountActivity;
import com.midas.app.models.Account;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

public class CreateAccountWorkflowImpl implements CreateAccountWorkflow {
  private final AccountActivity accountActivity;

  public CreateAccountWorkflowImpl(AccountActivity accountActivity) {
    this.accountActivity = accountActivity;
  }

  @Override
  public Account createAccount(Account details) {
    ActivityOptions options = ActivityOptions.newBuilder().setTaskQueue(QUEUE_NAME).build();
    Account account =
        Workflow.newActivityStub(AccountActivity.class, options).createPaymentAccount(details);
    return account;
  }
}
