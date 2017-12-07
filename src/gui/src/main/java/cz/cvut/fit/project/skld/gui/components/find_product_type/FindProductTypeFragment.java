package cz.cvut.fit.project.skld.gui.components.find_product_type;

import cz.cvut.fit.project.skld.client.exceptions.APIException;
import cz.cvut.fit.project.skld.gui.Fragment;
import cz.cvut.fit.project.skld.gui.Handler;
import cz.cvut.fit.project.skld.gui.NotifyType;
import cz.cvut.fit.project.skld.gui.Notifyable;
import cz.cvut.fit.project.skld.representations.ProductRepresentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindProductTypeFragment extends Fragment {
    private FindProductTypeFragmentHandler handler;

    private List<ProductRepresentation> productTypes;
    private ProductRepresentation selectedProductType;

    public FindProductTypeFragment(Notifyable parent) {
        super(parent);
        productTypes = new ArrayList<>();
    }

    @Override
    public Handler makeHandler() {
        this.handler = new FindProductTypeFragmentHandler();
        handler.setOwner(this);
        return handler;
    }

    public ProductRepresentation getSelected() {
        return selectedProductType;
    }

    protected void setSelectedProductType(ProductRepresentation selectedProductType) {
        if (this.selectedProductType != selectedProductType) {
            this.selectedProductType = selectedProductType;
            getParent().notify(this, NotifyType.CHANGE);
        }
    }

    public void refresh() {
        try {
            productTypes = getApp().getHttpClient().getProducts();
        } catch (IOException | APIException e) {
            e.printStackTrace();
            System.exit(1);
        }

//        productTypes.add(new ProductRepresentation((long)666666, "Wolrd Dominator Type B", (long)10));
        handler.setProductTypes(productTypes);

        handler.clearSelection();
    }
}