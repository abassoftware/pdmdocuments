package de.abas.pdmdocuments.coffee.infosystem;

import de.abas.erp.db.schema.part.Product;

public class ProductListitem {
	private Product product;
	private Integer stufe;

	public ProductListitem(Product product, Integer stufe) {
		super();
		this.product = product;
		this.stufe = stufe;
	}

	public Product getProduct() {
		return product;
	}

	public Integer getStufe() {
		return stufe;
	}

}
