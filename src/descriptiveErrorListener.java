import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class descriptiveErrorListener extends BaseErrorListener {
	    private static final boolean REPORT_SYNTAX_ERRORS = true;
	    public static String errors = "" ;
	    public static descriptiveErrorListener INSTANCE = new descriptiveErrorListener();

	    @Override
	    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
	                            int line, int charPositionInLine,
	                            String msg, RecognitionException e)
	    {
	        if (!REPORT_SYNTAX_ERRORS) {
	            errors = "";
	            return ;
	        }
	        
	        String sourceName = recognizer.getInputStream().getSourceName();
	        /*if (!sourceName.isEmpty()) {
	            sourceName = String.format("%s:%d:%d: ", sourceName, line, charPositionInLine);
	        }*/

	        errors = sourceName+"line "+line+":"+charPositionInLine+" "+msg;
	    }

	    /**
	     * @return the errors
	     */
	   
	}


