package pl.fryciarnia.user;

/**
 * Rodzaj użytkownika
 */
public enum UserType
{
    Admin,      /* Zarządza całym systemem z góry */
    Manager,    /* Właściciel lokalu */
    Kitchen,    /* Pracownik */
    Display,    /* Wyświetlacz w lokalu */
    Terminal,   /* Terminal do zamawiania */
    Web         /* Dowolny zewnętrzny urzytkownik */
}
