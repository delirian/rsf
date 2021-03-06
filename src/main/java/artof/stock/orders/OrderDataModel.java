package artof.stock.orders;
import artof.materials.MaterialDets;
import artof.materials.TypeMapper;
import artof.stock.StockItem;
import artof.database.ArtofDB;
import artof.utils.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class OrderDataModel extends DefaultTableModel {
  private ArrayList itemList = new ArrayList();
  private OrderInDialog dialog;
  TableItemCodeComboBox cbxItemCodes;

  public OrderDataModel(OrderInDialog d, TableItemCodeComboBox cbxItemCodes) {
    dialog = d;
    this.cbxItemCodes = cbxItemCodes;
  }

  public void refreshItems() {
    itemList = new ArrayList();
    fireTableDataChanged();
  }

  public ArrayList getItemList() {
    return itemList;
  }

  public int getColumnCount() {
    return 6;
  }

  public String getColumnName(int column) {
    if (column == 0) return "Type";
    else if (column == 1) return "Supplier";
    else if (column == 2) return "Item Code";
    else if (column == 3) return "Length";
    else if (column == 4) return "Width";
    else if (column == 5) return "No of Items";
    /*if (column == 0) return "Item Code";
    else if (column == 1) return "Supplier";
    else if (column == 2) return "Type";
    else if (column == 3) return "Length";
    else if (column == 4) return "Width";
    else if (column == 5) return "No of Items";*/
    else return null;
  }

  public int getRowCount() {
    try {
      return itemList.size();
    } catch (NullPointerException e) {
      return 0;
    }
  }

  public Object getValueAt(int row, int column) {
    try {
      StockItem item = (StockItem) itemList.get(row);

      if (column == 0) {
        return MaterialDets.getItemTypeString(item.getMatType());

      } else if (column == 1) {
        return item.getSupplier();

      } else if (column == 2) {
        return item.getItemCode();

      } else if (column == 3) {
        return Utils.getNumberFormat(item.getLength());

      } else if (column == 4) {
        return Utils.getNumberFormat(item.getWidth());

      } else if (column == 5) {
        return String.valueOf(item.getCount());

      } else {
        return null;
      }

    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public void setValueAt(Object aValue, int row, int column) {
    try {
      StockItem item = (StockItem)itemList.get(row);

      if (column == 0) {
        int type = MaterialDets.getItemTypeInt((String)aValue);
        if (item.getMatType() != type) {
          item.setItemCode(null);
        }
        item.setMatType(type);
        //cbxItemCodes.setMaterialType(type);

      } else if (column == 1) {
        item.setSupplier((String)aValue);

      } else if (column == 2) {
        item.setItemCode((String)aValue);

      } else if (column == 3) {
        item.setLength(Float.parseFloat((String)aValue));

      } else if (column == 4) {
        if (item.getMatType() != 3) {
          item.setWidth(Float.parseFloat((String)aValue));
        } else {
          item.setWidth(0);
        }
      } else if (column == 5) {
        item.setCount(Integer.parseInt((String)aValue));

      }

    } catch (java.lang.NumberFormatException e) {
        String mes = "Invalid value was specified.";
        JOptionPane.showMessageDialog(dialog, mes, "Error",
                                      JOptionPane.ERROR_MESSAGE);
    } catch (IndexOutOfBoundsException e) {
      //doen fokkol
    }
  }

  public void insertRow(int row, Object[] rowData) {
    StockItem item = new StockItem();
    itemList.add(item);
    fireTableDataChanged();
  }

  public void removeRow(int row) {
    try {
      String mes = "Are you sure you want to delete the item?";
      int res = JOptionPane.showConfirmDialog(dialog, mes, "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (res == JOptionPane.YES_OPTION) {
        StockItem item = (StockItem)itemList.get(row);
        ArtofDB db = ArtofDB.getCurrentDB();
        Iterator it = item.getCountList().iterator();
        while (it.hasNext()) {
          db.deleteStockItem(((Integer)it.next()).intValue());
        }
        db.deleteStockItem(item.getStockID());

        itemList.remove(row);
      }
    } catch (IndexOutOfBoundsException e) {
      //doen fokkol
    }
    fireTableDataChanged();
  }

  public boolean isCellEditable(int row, int column) {
    //if (column == 0)
    //  return false;
    //else
      return true;
  }

  public float getTotalAmount() {
    float amount = 0.f;
    //Iterator it = itemList.iterator();
    //while (it.hasNext()) {
      //StockItem item = (StockItem)it.next();
      //amount += item.getCount() * item.getCost();
    //}
    return amount;
  }

  public ArrayList getStockIDs(int row) {
    try {
      StockItem item = (StockItem)itemList.get(row);
      return item.getCountList();

    } catch (IndexOutOfBoundsException e) {
      return new ArrayList();
    }
  }
}