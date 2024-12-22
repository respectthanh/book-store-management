package application.bookstore.views;

import application.bookstore.controllers.ControllerCommon;
import application.bookstore.controllers.StatsController;
import application.bookstore.models.Book;
import application.bookstore.models.BookOrder;
import application.bookstore.models.Order;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public class StatsView extends View {



    public StatsView() {
        new StatsController(this);
    }

    @Override
    public Parent getView() {
        BorderPane borderPane = new BorderPane();
        VBox vBox = new VBox();
        vBox.setSpacing(50);
        vBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(new customPieChart(customPieChart.dataOptions.Client_Based_Total_Income),
                new customPieChart(customPieChart.dataOptions.User_Based_Total_Income),
                new customPieChart(customPieChart.dataOptions.Book_Based_Total_Income),
                new customPieChart(customPieChart.dataOptions.Book_Based_Units_Sold));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(vBox);
        scrollPane.setFitToWidth(true);
        borderPane.setCenter(scrollPane);
        return borderPane;
    }



    class customPieChart extends PieChart {

        public enum dataOptions{
            Client_Based_Total_Income,
            User_Based_Total_Income,
            Book_Based_Total_Income,
            Book_Based_Units_Sold,
        }

        public enum Unit{
            $,
            pcs
        }

        public customPieChart(dataOptions option) {
            ObservableList<Data> pieChartData = FXCollections.observableArrayList();

            ObservableList<Order> orders = Order.getOrders();
            orders.addListener(new ListChangeListener() {
                @Override
                public void onChanged(ListChangeListener.Change change) {
                    populateChartData(orders, pieChartData, option);
                }
            });

            populateChartData(orders, pieChartData, option);

            super.setData(pieChartData);
            super.setTitle(option.toString().replaceAll("_", " "));
        }


        private void populateChartData(List<Order> orders, ObservableList<PieChart.Data> pieChartData, dataOptions option) {
            pieChartData.clear();

            if (option==dataOptions.User_Based_Total_Income)
                groupByUser(orders, pieChartData);
            else if (option==dataOptions.Client_Based_Total_Income)
                groupByClientName(orders, pieChartData);
            else if (option==dataOptions.Book_Based_Total_Income)
                incomeGroupByBook(orders, pieChartData);
            else if (option==dataOptions.Book_Based_Units_Sold)
                unitsGroupByBook(orders, pieChartData);
        }

        private void addUnit(ObservableList<PieChart.Data> pieChartData, Unit u){
            if (u==Unit.$)
                pieChartData.forEach(data ->
                        data.nameProperty().bind(
                                Bindings.concat(
                                        data.getName(), " ", Math.round(data.pieValueProperty().getValue()*100)/100.f, u.toString()
                                )
                        )
                );
            else if (u==Unit.pcs)
                pieChartData.forEach(data ->
                        data.nameProperty().bind(
                                Bindings.concat(
                                        data.getName(), " ", data.pieValueProperty(), u.toString()
                                )
                        )
                );
        }

        private void groupByUser(List<Order> orders, ObservableList<PieChart.Data> pieChartData){
            for (Order o : orders) {
                boolean match = false;
                for (PieChart.Data d : pieChartData) {
                    if (o.getUsername().equals(d.getName())) {
                        d.setPieValue(Math.round(d.getPieValue()+o.getTotal()));
                        match = true;
                        break;
                    }
                }
                if (!match)
                    pieChartData.add(new PieChart.Data(o.getUsername(), o.getTotal()));
            }
            addUnit(pieChartData, Unit.$);
        }

        private void groupByClientName(List<Order> orders, ObservableList<PieChart.Data> pieChartData){
            for (Order o : orders) {
                boolean match = false;
                for (PieChart.Data d : pieChartData) {
                    if (o.getClientName().equals(d.getName())) {
                        d.setPieValue(d.getPieValue()+o.getTotal());
                        match = true;
                        break;
                    }
                }
                if (!match)
                    pieChartData.add(new PieChart.Data(o.getClientName(), o.getTotal()));
            }
            addUnit(pieChartData, Unit.$);
        }

        private void incomeGroupByBook(List<Order> orders, ObservableList<PieChart.Data> pieChartData){
            class data {
                String title;
                String isbn;
                float total;
                data(String title, String isbn, float total){
                    this.title=title;
                    this.isbn=isbn;
                    this.total=total;
                }
            }
            List<data> data_ = new ArrayList<>();

            for (Order o : orders) {
                for (BookOrder b : o.getBooksOrdered()) {
                    boolean match = false;
                    for (data d : data_) {
                        if (b.getBookISBN().equals(d.isbn)) {
                            d.total += b.getTotalPrice();
                            match = true;
                            break;
                        }
                    }
                    if (!match)
                        data_.add(new data(b.getTitle(), b.getBookISBN(), b.getTotalPrice()));
                }
            }

            for (data d : data_)
                pieChartData.add(new Data(d.title, d.total));

            addUnit(pieChartData, Unit.$);
        }

        private void unitsGroupByBook(List<Order> orders, ObservableList<PieChart.Data> pieChartData){
            class data {
                String title;
                String isbn;
                int quantity;
                data(String title, String isbn, int quantity){
                    this.title=title;
                    this.isbn=isbn;
                    this.quantity=quantity;
                }
            }
            List<data> data_ = new ArrayList<>();

            for (Order o : orders) {
                for (BookOrder b : o.getBooksOrdered()) {
                    boolean match = false;
                    for (data d : data_) {
                        if (b.getBookISBN().equals(d.isbn)) {
                            d.quantity += b.getQuantity();
                            match = true;
                            break;
                        }
                    }
                    if (!match)
                        data_.add(new data(b.getTitle(), b.getBookISBN(), b.getQuantity()));
                }
            }

            for (data d : data_)
                pieChartData.add(new Data(d.title, d.quantity));
            addUnit(pieChartData, Unit.pcs);
        }

    }
}
