package pl.fryciarnia.order;

public enum OrderStatus
{
  PAYING, /* user is paying for the order */
  PAID,   /* user paid, kitchen is cooking */
  FAILED, /* something went totally wrong */
  READY,  /* order is ready, waiting for user to take it out */
  DONE,   /* user has received their meal */
}
