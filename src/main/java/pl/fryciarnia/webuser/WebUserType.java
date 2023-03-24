package pl.fryciarnia.webuser;

public enum WebUserType
{
    Admin,      /* Zarządza całym systemem z góry */
    Manager,    /* Właściciel lokalu */
    Kitchen,    /* Pracownik */
    Display,    /* Wyświetlacz w lokalu */
    Terminal,   /* Terminal do zamawiania */
    Web         /* Dowolny zewnętrzny urzytkownik */
}
