package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;


public class EditOrAddToDoItemActivity extends Activity
{
	public int position=0;
	EditText etItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//populate the screen using the layout
		setContentView(R.layout.activity_edit_item);
		
		//Get the data from the main screen
		String editItem = getIntent().getStringExtra("item");
		position = getIntent().getIntExtra("position",-1);
		
		// show original content in the text field
		etItem = (EditText)findViewById(R.id.etEditItem);
		etItem.setText(editItem);
	}

	/**
	 * onSubmit
	 *
	 * <p> send item and .
	 *
	 * <pre>{@code
	 *  演示如何使用该方法
	 *  List<Goods> items = new ArrayList<>();
	 *  Goods goods = new Goods(1L, BigDecimal.ONE);
	 *  Goods goods2 = new Goods(2L, BigDecimal.TEN);
	 *  items.add(goods);
	 *  items.add(goods2);
	 *
	 *  Order order1 = new Order();
	 *  order.setUserId("1");
	 *  order.setItems(items);
	 *  OrderService#createOrder(order);
	 * }
	 * </pre>
	 *
	 * @param order 订单信息
	 * @return 是否创建成功
	 * @version 1.0
	 */
	public void onSubmit(View v) {
		etItem = (EditText) findViewById(R.id.etEditItem);

		// Prepare data intent for sending it back
		Intent data = new Intent();

		// Pass relevant data back as a result
		data.putExtra("item", etItem.getText().toString());
		data.putExtra("position", position);

		// Activity finished ok, return the data
		setResult(RESULT_OK, data); // set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	}

	public void onCancel(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(EditOrAddToDoItemActivity.this);
		builder.setTitle(R.string.dialog_cancel_edit_title)
				.setMessage(R.string.dialog_cancel_edit_msg)
				.setPositiveButton(R.string.cancel_edit_yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						backToMainActivity();
					}
				})
				.setNegativeButton(R.string.cancel_edit_no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						// User cancelled the dialog
						// Nothing happens
					}
				});

		builder.create().show();
	}

	public void backToMainActivity(){
		// Prepare data intent for sending it back
		Intent data = new Intent();
		// Activity finished ok, return the data
		setResult(RESULT_CANCELED, data);// set result code and bundle data for response
		finish(); // closes the activity, pass data to parent
	}
}
