package de.abas.pdmdocuments.infosystem;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.abas.erp.db.ContextManager;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.SelectableObject;
import de.abas.erp.db.SelectableRecord;
import de.abas.erp.db.TableDescriptor;
import de.abas.erp.db.TableDescriptor.FieldQuantum;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments;
import de.abas.erp.db.infosystem.custom.owpdm.PdmDocuments.Row;
import de.abas.erp.db.infosystem.standard.st.MultiLevelBOM;
import de.abas.erp.db.schema.part.Product;
import de.abas.erp.db.schema.part.SelectablePart;
import de.abas.erp.db.schema.purchasing.BlanketOrder;
import de.abas.erp.db.schema.purchasing.Invoice;
import de.abas.erp.db.schema.purchasing.PackingSlip;
import de.abas.erp.db.schema.purchasing.PurchaseOrder;
import de.abas.erp.db.schema.purchasing.Purchasing;
import de.abas.erp.db.schema.purchasing.Request;
import de.abas.erp.db.schema.sales.CostEstimate;
import de.abas.erp.db.schema.sales.Opportunity;
import de.abas.erp.db.schema.sales.Quotation;
import de.abas.erp.db.schema.sales.RepairOrder;
import de.abas.erp.db.schema.sales.Sales;
import de.abas.erp.db.schema.sales.SalesOrder;
import de.abas.erp.db.schema.sales.ServiceOrder;
import de.abas.erp.db.schema.sales.ServiceQuotation;
import de.abas.erp.db.schema.sales.WebOrder;
import de.abas.erp.db.schema.workorder.WorkOrders;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.ExpertSelection;
import de.abas.erp.db.selection.Selection;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.db.util.ContextHelper;
import de.abas.pdmdocuments.infosystem.utils.UtilwithAbasConnection;

public class SearchMaschineProduct {

	private static final Integer MAX_TREELEVEL = 999;
	private static final Logger log = Logger.getLogger(SearchMaschineProduct.class);

	private SearchMaschineProduct() {
		throw new UnsupportedOperationException();
	}

	protected static List<Product> getProducts(SelectableObject beleg, DbContext ctx) {
		ArrayList<Product> listProduct = new ArrayList<>();

		if (beleg instanceof Sales) {

			listProduct = productsfromSalesBlanketOrder(beleg, ctx);
			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesQuotation(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesInvoice(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesInvoice(beleg, ctx);
			}
			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesWebOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesServiceQuotation(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesServiceOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesRepairOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesOpportunity(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromSalesCostEstimate(beleg, ctx);
			}

		} else if (beleg instanceof Purchasing) {

			listProduct = productsfromPurchaseBlanketOrder(beleg, ctx);

			if (listProduct.isEmpty()) {
				listProduct = productsfromPurchaseOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromPurchaseOrder(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromRequest(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromPurchasePackingSlip(beleg, ctx);
			}

			if (listProduct.isEmpty()) {
				listProduct = productsfromPurchaseInvoice(beleg, ctx);
			}

		} else if (beleg instanceof WorkOrders) {
			WorkOrders sbeleg = (WorkOrders) beleg;
			Product product = getProduct(sbeleg.getProduct());
			if (product != null) {
				listProduct.add(product);
			}

		}

		return listProduct;
	}

	private static ArrayList<Product> productsfromPurchaseOrder(SelectableObject beleg, DbContext ctx) {

		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, PurchaseOrder.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromSalesOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, SalesOrder.Row.META.product.getName());

	}

	private static ArrayList<Product> productsfromSalesWebOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, WebOrder.Row.META.product.getName());

	}

	private static ArrayList<Product> productsfromPurchaseBlanketOrder(SelectableObject beleg, DbContext ctx) {
		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, BlanketOrder.Row.META.product.getName());

	}

	private static ArrayList<Product> productsfromSalesQuotation(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, Quotation.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromSalesCostEstimate(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, CostEstimate.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromSalesOpportunity(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, Opportunity.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromSalesServiceQuotation(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, ServiceQuotation.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromSalesServiceOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, ServiceOrder.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromSalesRepairOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, RepairOrder.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromSalesBlanketOrder(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group,
				de.abas.erp.db.schema.sales.BlanketOrder.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromPurchasePackingSlip(SelectableObject beleg, DbContext ctx) {
		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, PackingSlip.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromRequest(SelectableObject beleg, DbContext ctx) {
		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, Request.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromPurchaseInvoice(SelectableObject beleg, DbContext ctx) {
		int database = 4;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group, Invoice.Row.META.product.getName());
	}

	private static ArrayList<Product> productsfromSalesInvoice(SelectableObject beleg, DbContext ctx) {
		int database = 3;
		int group = 2;

		return getProductsFromSalesOrPurchase(beleg, ctx, database, group,
				de.abas.erp.db.schema.sales.Invoice.Row.META.product.getName());

	}

	protected static Product getProduct(SelectablePart selProduct) {
		if (selProduct instanceof Product) {
			return (Product) selProduct;
		} else {
			return null;
		}
	}

	private static ArrayList<Product> getProductsFromSalesOrPurchase(SelectableObject beleg, DbContext ctx,
			int database, int group, String productFieldName) {

		ArrayList<Product> listProduct = new ArrayList<Product>();
		String criteria = "head=" + beleg.getId().toString() + ";@filingmode=(Both)";
		log.info(UtilwithAbasConnection.getMessage("start.selection.SalesOrPurchase", System.currentTimeMillis(),
				criteria));
		Selection<? extends SelectableRecord> selectionStorage = ExpertSelection
				.create(new TableDescriptor(database, group, FieldQuantum.Table), criteria);
		de.abas.erp.db.Query<? extends SelectableRecord> query = ctx.createQuery(selectionStorage);

		for (SelectableRecord row : query) {
			String productString = row.getString(productFieldName);
			Product product = getProduct(productString, ctx);
			if (product != null) {
				listProduct.add(product);
			}
		}
		log.info(UtilwithAbasConnection.getMessage("end.selection.SalesOrPurchase", System.currentTimeMillis(),
				criteria));
		return listProduct;
	}

	protected static Product getProduct(String productString, DbContext ctx) {
		String criteria = "id=" + productString + ";@file=2:1";
		Selection<Product> select = ExpertSelection.create(Product.class, criteria);
		de.abas.erp.db.Query<Product> queryProduct = ctx.createQuery(select);
		Product productsel = null;
		for (Product product : queryProduct) {
			if (productsel == null) {
				productsel = product;
			} else {
				// Es darf nur einen Treffer geben.
				return null;
			}

		}

		return productsel;
	}

	protected static Iterable<Product> getProductsFromString(String productString, DbContext ctx)
			throws PdmDocumentsException {
		// trenner ;
		ArrayList<Product> productList = new ArrayList<Product>();

		String[] productStringlist = productString.split(";");
		for (String productNumber : productStringlist) {
			SelectionBuilder<Product> selectionBuilder = SelectionBuilder.create(Product.class);
			selectionBuilder.add(Conditions.eq(Product.META.idno, productNumber));
			de.abas.erp.db.Query<Product> queryproduct = ctx.createQuery(selectionBuilder.build());
			List<Product> productQueryList = queryproduct.execute();

			if (productQueryList.isEmpty()) {
				throw new PdmDocumentsException(
						UtilwithAbasConnection.getMessage("error.sammellist.productnotfound", productNumber));
			}
			if (productQueryList.size() > 1) {
				throw new PdmDocumentsException(
						UtilwithAbasConnection.getMessage("error.sammellist.productnotunique", productNumber));
			}
			productList.add(productQueryList.get(0));

		}

		return productList;
	}

	protected static void insertProductStructureInRow(PdmDocuments head, Row row) {
		int aktrow = row.getRowNo() + 1;
		int treelevel = row.getYtstufe();
		int sstltreelevel = head.getYstufe();
		Product product = row.getYtartikel();
		if (sstltreelevel == 0) {
			sstltreelevel = MAX_TREELEVEL;
		}

		ArrayList<ProductListitem> productListitemList = getbomproducts(product, sstltreelevel);

		for (ProductListitem productListitem : productListitemList) {
			Row insertRow = head.table().insertRow(aktrow);
			aktrow++;
			insertRow.setYtartikel(productListitem.getProduct());
			insertRow.setYtstufe(productListitem.getStufe() + treelevel);
		}
	}

	private static ArrayList<ProductListitem> getbomproducts(Product product, Integer maxStufe) {
		ArrayList<ProductListitem> productList = new ArrayList<>();
		ContextManager contextmanager = ContextHelper.buildContextManager();
		DbContext dbcontext = contextmanager.getServerContext();
		MultiLevelBOM mlb = dbcontext.openInfosystem(MultiLevelBOM.class);
		mlb.setArtikel(product);
		mlb.setCountLevels(maxStufe);
		mlb.setBmitag(false);
		mlb.setBmitfm(false);
		mlb.invokeStart();
		Iterable<de.abas.erp.db.infosystem.standard.st.MultiLevelBOM.Row> mlbRows = mlb.table().getRows();
		for (de.abas.erp.db.infosystem.standard.st.MultiLevelBOM.Row row : mlbRows) {
			int treeLevel = row.getTreeLevel();
			SelectableObject selProduct = row.getElem();
			if (selProduct instanceof Product) {
				Product productInPosition = (Product) selProduct;
				ProductListitem productListitem = new ProductListitem(productInPosition, treeLevel);
				productList.add(productListitem);
			}
		}
		mlb.close();
		return productList;

	}

	protected static void loadProductsInTable(PdmDocuments head, DbContext ctx) {

		if (head.getYbeleg() == null) {

			if (head.getYartikel() != null) {
				insertProductInRow(head.getYartikel(), head);
			}

		} else {
			SelectableObject beleg = head.getYbeleg();
			List<Product> listProduct = SearchMaschineProduct.getProducts(beleg, ctx);
			for (Product product : listProduct) {
				insertProductInRow(product, head);
			}

		}

		if (head.getYstruktur()) {
			List<Row> tableRows = head.getTableRows();
			for (Row row : tableRows) {
				if (row.getYtstufe() == 1) {
					SearchMaschineProduct.insertProductStructureInRow(head, row);
				}

			}
		}

	}

	protected static void insertProductInRow(Product product, PdmDocuments head) {
		Row row = head.table().appendRow();
		row.setYtartikel(product);
		row.setYtstufe(1);
	}

}
