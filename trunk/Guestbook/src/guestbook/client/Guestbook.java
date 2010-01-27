package guestbook.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Guestbook implements EntryPoint {
	
	private static final int REFRESH_INTERVAL = 5000; // ms

	private VerticalPanel mainPanel = new VerticalPanel();
	  private FlexTable stocksFlexTable = new FlexTable();
	  private HorizontalPanel addPanel = new HorizontalPanel();
	  private TextBox newSymbolTextBox = new TextBox();
	  private Button addStockButton = new Button("Add");
	  private Label lastUpdatedLabel = new Label();
	  private ArrayList<String> stocks = new ArrayList<String>();


	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				sendButton.setEnabled(false);
				String textToServer = nameField.getText();
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
		
		
		
		
		
		 // Create table for stock data.
	    stocksFlexTable.setText(0, 0, "Symbol");
	    stocksFlexTable.setText(0, 1, "Price");
	    stocksFlexTable.setText(0, 2, "Change");
	    stocksFlexTable.setText(0, 3, "Remove");
	    
	    
	 // Assemble Add Stock panel.
	    addPanel.add(newSymbolTextBox);
	    addPanel.add(addStockButton);

	    // Assemble Main panel.
	    mainPanel.add(stocksFlexTable);
	    mainPanel.add(addPanel);
	    mainPanel.add(lastUpdatedLabel);
	 // Associate the Main panel with the HTML host page.

	    RootPanel.get("stockList").add(mainPanel);
	    // Move cursor focus to the input box.
	    newSymbolTextBox.setFocus(true);
	    
	 // Setup timer to refresh list automatically.
	    Timer refreshTimer = new Timer() {
	      @Override
	      public void run() {
	        refreshWatchList();
	      }
	    };
	    refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

	    addStockButton.addClickHandler(new ClickHandler() {
	        public void onClick(ClickEvent event) {
	          addStock();
	        }
	      });


	    // Listen for keyboard events in the input box.
	    newSymbolTextBox.addKeyPressHandler(new KeyPressHandler() {
			
			
	      public void onKeyPress(KeyPressEvent event) {
	        if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	          addStock();
	        }
	      }
	    });

		
	}
	
	 /**
	   * Add stock to FlexTable. Executed when the user clicks the addStockButton or
	   * presses enter in the newSymbolTextBox.
	   */
	  private void addStock() {
		  final String symbol = newSymbolTextBox.getText().toUpperCase().trim();
		    newSymbolTextBox.setFocus(true);

		    // Stock code must be between 1 and 10 chars that are numbers, letters, or dots.
		    if (!symbol.matches("^[0-9A-Z\\.]{1,10}$")) {
		      Window.alert("'" + symbol + "' is not a valid symbol.");
		      newSymbolTextBox.selectAll();
		      return;
		    }

		    newSymbolTextBox.setText("");

		    // TODO Don't add the stock if it's already in the table.
		    // Don't add the stock if it's already in the table.
		    if (stocks.contains(symbol))
		      return;
		    // TODO Add the stock to the table.
		 // Add the stock to the table.
		    int row = stocksFlexTable.getRowCount();
		    stocks.add(symbol);
		    stocksFlexTable.setText(row, 0, symbol);
		    // TODO Add a button to remove this stock from the table.
		 // Add a button to remove this stock from the table.
		    Button removeStockButton = new Button("x");
		    removeStockButton.addClickHandler(new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        int removedIndex = stocks.indexOf(symbol);
		        stocks.remove(removedIndex);
		        stocksFlexTable.removeRow(removedIndex + 1);
		      }
		    });
		    stocksFlexTable.setWidget(row, 3, removeStockButton);
		 // Get the stock price.
		    refreshWatchList();



	  }
	  
	  private void refreshWatchList() {
		  final double MAX_PRICE = 100.0; // $100.00
		    final double MAX_PRICE_CHANGE = 0.02; // +/- 2%

		    StockPrice[] prices = new StockPrice[stocks.size()];
		    for (int i = 0; i < stocks.size(); i++) {
		      double price = Random.nextDouble() * MAX_PRICE;
		      double change = price * MAX_PRICE_CHANGE
		          * (Random.nextDouble() * 2.0 - 1.0);

		      prices[i] = new StockPrice(stocks.get(i), price, change);
		    }

		    updateTable(prices);


		  }
	  
	  private void updateTable(StockPrice[] prices) {
		  for (int i = 0; i < prices.length; i++) {
		      updateTable(prices[i]);
		    }
		  // Display timestamp showing last refresh.
		    lastUpdatedLabel.setText("Last update : "
		        + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));



		  }

	private void updateTable(StockPrice price) {
		 // Make sure the stock is still in the stock table.
	    if (!stocks.contains(price.getSymbol())) {
	      return;
	    }

	    int row = stocks.indexOf(price.getSymbol()) + 1;

	    // Format the data in the Price and Change fields.
	    String priceText = NumberFormat.getFormat("#,##0.00").format(
	        price.getPrice());
	    NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
	    String changeText = changeFormat.format(price.getChange());
	    String changePercentText = changeFormat.format(price.getChangePercent());

	    // Populate the Price and Change fields with new data.
	    stocksFlexTable.setText(row, 1, priceText);
	    stocksFlexTable.setText(row, 2, changeText + " (" + changePercentText
	        + "%)");

		
	}

}

class StockPrice {

	  private String symbol;
	  private double price;
	  private double change;

	  public StockPrice() {
	  }

	  public StockPrice(String symbol, double price, double change) {
	    this.symbol = symbol;
	    this.price = price;
	    this.change = change;
	  }

	  public String getSymbol() {
	    return this.symbol;
	  }

	  public double getPrice() {
	    return this.price;
	  }

	  public double getChange() {
	    return this.change;
	  }

	  public double getChangePercent() {
	    return 100.0 * this.change / this.price;
	  }

	  public void setSymbol(String symbol) {
	    this.symbol = symbol;
	  }

	  public void setPrice(double price) {
	    this.price = price;
	  }

	  public void setChange(double change) {
	    this.change = change;
	  }
	}
