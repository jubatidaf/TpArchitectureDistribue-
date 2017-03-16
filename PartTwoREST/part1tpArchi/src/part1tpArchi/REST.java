package part1tpArchi;

import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.*;
import javax.xml.ws.http.HTTPBinding;

import com.sun.jersey.core.impl.provider.entity.SourceProvider;
@WebServiceProvider
@ServiceMode(value = Service.Mode.PAYLOAD)
public class REST implements Provider<Source> {
	String message;
	public REST(String message){
		this.message=message;
	}
public Source invoke(Source source) {
StreamSource reply=new StreamSource();
String replyElement = new String("<p>hello world</p>");
StreamSource reply1 = new StreamSource(new StringReader(replyElement));
String replyElement2 = new String("<p>Université de Rouen</p>");
StreamSource reply2 = new StreamSource(new StringReader(replyElement2));
String replyElement3 = new String("<p>Reponse du service rest</p>");
StreamSource reply3 = new StreamSource(new StringReader(replyElement3));
if (this.message=="hw")
	reply=reply1;
	else if (this.message=="ur")
		reply=reply2;
	else if (this.message=="rsr")
		reply=reply3;
	
return reply;
}
public static void main(String args[]) {
Endpoint e = Endpoint.create(HTTPBinding.HTTP_BINDING, new REST("hw"));
e.publish("http://127.0.0.1:8084/hello/world");
e = Endpoint.create(HTTPBinding.HTTP_BINDING, new REST("ur"));
e.publish("http://127.0.0.1:8089/test");
e = Endpoint.create(HTTPBinding.HTTP_BINDING, new REST("rsr"));
e.publish("http://127.0.0.1:8090/hello/world");
}
}