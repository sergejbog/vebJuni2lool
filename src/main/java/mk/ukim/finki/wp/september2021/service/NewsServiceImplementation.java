package mk.ukim.finki.wp.september2021.service;

import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsCategory;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsCategoryIdException;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsIdException;
import mk.ukim.finki.wp.september2021.repository.NewsCategoryRepository;
import mk.ukim.finki.wp.september2021.repository.NewsRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImplementation implements NewsService{

    NewsRepository newsRepository;
    NewsCategoryRepository newsCategoryRepository;

    public NewsServiceImplementation(NewsRepository newsRepository, NewsCategoryRepository newsCategoryRepository) {
        this.newsRepository = newsRepository;
        this.newsCategoryRepository = newsCategoryRepository;
    }

    @Override
    public List<News> listAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
    }

    @Override
    public News create(String name, String description, Double price, NewsType type, Long category) {
        NewsCategory newsCategory = newsCategoryRepository.findById(category).orElseThrow(InvalidNewsCategoryIdException::new);
        News news = new News(name,description,price,type,newsCategory);
        return newsRepository.save(news);
    }

    @Override
    public News update(Long id, String name, String description, Double price, NewsType type, Long category) {
        NewsCategory newsCategory = newsCategoryRepository.findById(category).orElseThrow(InvalidNewsCategoryIdException::new);
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsCategoryIdException::new);
        news.setName(name);
        news.setDescription(description);
        news.setPrice(price);
        news.setType(type);
        news.setCategory(newsCategory);
        return newsRepository.save(news);
    }

    @Override
    public News delete(Long id) {
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
        newsRepository.deleteById(id);
        return news;
    }

    @Override
    public News like(Long id) {
        News news = newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
        news.setLikes(news.getLikes()+1);
        return newsRepository.save(news);
    }

    @Override
    public List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type) {
        if(price == null){
            price = Double.MAX_VALUE;
        }
        System.out.println(type);
        if(type != null){

            return newsRepository.findByPriceLessThanAndType(price,type);
        }
        return newsRepository.findByPriceLessThan(price);
    }
}
