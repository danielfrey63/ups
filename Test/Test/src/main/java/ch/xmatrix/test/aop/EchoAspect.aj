package ch.xmatrix.test.aop;

public aspect EchoAspect
{
    pointcut printSystemOut():
        execution(@ch.xmatrix.text.EchoAnnotation * *(..)) ||
        execution(* (@ch.xmatrix.text.EchoAnnotation *).*(..));

    before():
    printSystemOut()
    {
        System.out.println( "Hallo" );
    }
}
