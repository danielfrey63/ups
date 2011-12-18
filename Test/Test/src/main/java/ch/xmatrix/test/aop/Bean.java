package ch.xmatrix.test.aop;

/**
 * @author Daniel Frey 22.07.11 16:01
 */
public class Bean
{
    private String firstName;
    private String lastName;

    public String getFirstName()
    {
        return firstName;
    }

    @EchoAnnotation
    public void setFirstName( final String firstName )
    {
        this.firstName = firstName;
        System.out.println( "set" );
    }
}
