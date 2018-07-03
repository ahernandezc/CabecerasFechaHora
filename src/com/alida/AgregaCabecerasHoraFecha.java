package com.alida;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
 


import com.sonicsw.xq.*;

public class AgregaCabecerasHoraFecha implements XQServiceEx {

	
	// This is the XQLog (the container's logging mechanism).
    private XQLog m_xqLog = null;

    //This is the the log prefix that helps identify this service during logging
    private String m_logPrefix = "";
    
    //These hold version information.
    private static int s_major = 1;
    private static int s_minor = 0;
    private static int s_buildNumber = 0;
    private static final String TIME_FORMAT_NOW = "HH:mm:ss";
    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd";
    private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat tsdf = new SimpleDateFormat(TIME_FORMAT_NOW);
	private SimpleDateFormat dsdf = new SimpleDateFormat(DATE_FORMAT_NOW);

    /**
     * Constructor for a AgregaCabecerasHoraFecha
     */
	public AgregaCabecerasHoraFecha () {	
	}
	
	/**
     * Initialize the XQService by processing its initialization parameters.
     *
     * <p> This method implements a required XQService method.
     *
     * @param initialContext The Initial Service Context provides access to:<br>
     * <ul>
     * <li>The configuration parameters for this instance of the AgregaCabecerasHoraFecha.</li>
     * <li>The XQLog for this instance of the AgregaCabecerasHoraFecha.</li>
     * </ul>
     * @exception XQServiceException Used in the event of some error.
     */
    public void init(XQInitContext initialContext) throws XQServiceException {
    	XQParameters params = initialContext.getParameters();
        m_xqLog = initialContext.getLog();
        setLogPrefix(params);
        m_xqLog.logInformation(m_logPrefix + " Initializing ...");

        writeStartupMessage(params);
        writeParameters(params);
        //Perform initialization work.
	        
        m_xqLog.logInformation(m_logPrefix +" Initialized ...");
    }


    /**
     * Handle the arrival of XQMessages in the INBOX.
     *
     * <p> This method implements a required XQService method.
     *
     * @param ctx The service context.
     * @exception XQServiceException Thrown in the event of a processing error.
     */
    public void service(XQServiceContext ctx) throws XQServiceException {
		m_xqLog.logDebug(m_logPrefix + "Service processing...");

		// Get the message.
		XQEnvelope env = ctx.getNextIncoming();
		if (env != null) {
			XQMessage msg = env.getMessage();
			try {
				    XQPart prt = msg.getPart(0);
					Object content = prt.getContent();
                    m_xqLog.logInformation("Content at Part [" + 0 + "]:\n" + content);
                    msg.setHeaderValue("hora_recibido", tsdf.format(cal.getTime()).toString().replace(":", ""));
                    msg.setHeaderValue("fecha_recibido", dsdf.format(cal.getTime()));
				
			} catch (XQMessageException me) {
				throw new XQServiceException("Exception accessing XQMessage: "
						+ me.getMessage(), me);
			}

			// Pass message onto the outbox.
			Iterator<XQAddress> addressList = env.getAddresses();
			if (addressList.hasNext()) {
				// Add the message to the Outbox
				ctx.addOutgoing(env);
			}
		}
		m_xqLog.logDebug(m_logPrefix + "Service processed...");
	}
    
    /**
     * Clean up and get ready to destroy the service.
     *
     * <p> This method implement a required XQService method.
     */
    public void destroy() {
		m_xqLog.logInformation(m_logPrefix + "Destroying...");
		m_xqLog.logInformation(m_logPrefix + "Destroyed...");
    }
    
    /**
     * Called by the container on container start.
     *
     * <p> This method implement a required XQServiceEx method.
     */
	public void start() {
		m_xqLog.logInformation(m_logPrefix + "Starting...");
		m_xqLog.logInformation(m_logPrefix + "Started...");
	}
	
    /**
     * Called by the container on container stop.
     *
     * <p> This method implement a required XQServiceEx method.
     */
	public void stop() {
		m_xqLog.logInformation(m_logPrefix + "Stopping...");
		m_xqLog.logInformation(m_logPrefix + "Stopped...");
	}
	
	/**
     * Sets the prefix for XQLog for this instance of the service AgregaCabecerasHoraFecha
     */
	protected void setLogPrefix(XQParameters params) {
		String serviceName = params.getParameter(XQConstants.PARAM_SERVICE_NAME, XQConstants.PARAM_STRING);
		m_logPrefix = "[ " +  serviceName + " ]";
	}
	
    /**
     * Provide access to the service implemented version.
     *
     */
	protected String getVersion(){
		return s_major + "." + s_minor + ". build " + s_buildNumber;  
	}
	
    /**
     * Writes a standard service startup message to the log.
     */
    protected void writeStartupMessage(XQParameters params) {
        final StringBuffer buffer = new StringBuffer();
        String serviceTypeName = params.getParameter(XQConstants.SERVICE_PARAM_SERVICE_TYPE, XQConstants.PARAM_STRING);
        buffer.append("\n\n");
        buffer.append("\t\t " + serviceTypeName + "\n ");
        buffer.append("\t\t Version ");
        buffer.append(" " +getVersion());
        buffer.append("\n");
        m_xqLog.logInformation(buffer.toString());
	}
    
    /**
     * Writes parameters to log.
     */
    protected void writeParameters(XQParameters params) {
        final Map<String,XQParameterInfo> map = params.getAllInfo();
        final Iterator<XQParameterInfo> iter = map.values().iterator();

        while (iter.hasNext()) {
            final XQParameterInfo info = (XQParameterInfo) iter.next();

            if (info.getType() == XQConstants.PARAM_XML) {
                m_xqLog.logInformation( m_logPrefix + "Parameter Name =  " + info.getName());
            } else if (info.getType() == XQConstants.PARAM_STRING) {
            	m_xqLog.logInformation( m_logPrefix + "Parameter Name = " + info.getName());
            }

            if (info.getRef() != null) {
            	m_xqLog.logInformation(m_logPrefix +"Parameter Reference = " + info.getRef());

            	//If this is too verbose
            	///then a simple change from logInformation to logDebug
            	//will ensure file content is not displayed
            	//unless the logging level is set to debug for the ESB Container.
            	m_xqLog.logInformation(m_logPrefix +"----Parameter Value Start--------");
            	m_xqLog.logInformation("\n" +  info.getValue() +"\n");
            	m_xqLog.logInformation(m_logPrefix +"----Parameter Value End--------");
            }else{
            	m_xqLog.logInformation(m_logPrefix +"Parameter Value = " + info.getValue());            	
            }
        }
    }
}