package ru.tinkoff.edu.java.bot.scrapperapi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.scrapperapi.exception.ApiClientErrorException;
import ru.tinkoff.edu.java.bot.scrapperapi.exception.ApiInternalServerErrorException;
import ru.tinkoff.edu.java.bot.scrapperapi.model.AddLinkRequest;
import ru.tinkoff.edu.java.bot.scrapperapi.model.AllLinksApiResponse;
import ru.tinkoff.edu.java.bot.scrapperapi.model.LinkResponse;
import ru.tinkoff.edu.java.bot.scrapperapi.model.RemoveLinkRequest;
import ru.tinkoff.edu.java.common.model.ApiErrorResponse;
import static org.springframework.http.HttpMethod.DELETE;

public class HttpScrapperClient implements ScrapperClient {

    public static final String TG_CHAT_ID_HEADER = "Tg-Chat-Id";
    private static final Logger LOGGER = LogManager.getLogger(HttpScrapperClient.class);
    private static final String BASE_URL = "http://localhost:8080";
    public static final String START_TG_CHAT_URL = "/tg-chat/{id}/{username}";
    public static final String TG_CHAT_URL = "/tg-chat/{id}";
    public static final String LINKS_URL = "/links";
    public static final String WHEN_REGISTERING_NEW_CHAT = "registering new chat";
    public static final String WHEN_DELETING_CHAT = "deleting chat";
    public static final String WHEN_GETTING_TRACKING_LINKS = "getting tracking links";
    public static final String WHEN_ADDING_LINK_TO_TRACK = "adding link to track";
    public static final String WHEN_REMOVING_LINK_FROM_TRACKING = "removing link from tracking";

    private final String baseUrl;
    private final WebClient webClient;

    public HttpScrapperClient(WebClient webClient) {
        this.webClient = webClient;
        baseUrl = BASE_URL;
    }

    public HttpScrapperClient(String baseUrl, WebClient webClient) {
        this.baseUrl = baseUrl;
        this.webClient = webClient;
    }

    @Override
    public void registerChat(long chatId, String username) {
        webClient.post()
                .uri(baseUrl, uriBuilder -> uriBuilder.path(START_TG_CHAT_URL).build(chatId, username))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        resp -> onClientErrorInternal(resp, WHEN_REGISTERING_NEW_CHAT)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> onServerErrorInternal(resp, WHEN_REGISTERING_NEW_CHAT)
                )
                .bodyToMono(Void.class).block();
    }

    @Override
    public void deleteChat(long chatId) {
        webClient.delete()
                .uri(baseUrl, uriBuilder -> uriBuilder.path(TG_CHAT_URL).build(chatId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        resp -> onClientErrorInternal(resp, WHEN_DELETING_CHAT)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> onServerErrorInternal(resp, WHEN_DELETING_CHAT)
                )
                .bodyToMono(Void.class).block();
    }

    @Override
    public AllLinksApiResponse getAllLinks(long tgChatId) {
        return webClient
                .get()
                .uri(baseUrl, uriBuilder -> uriBuilder.path(LINKS_URL).build())
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        resp -> onClientErrorInternal(resp, WHEN_GETTING_TRACKING_LINKS)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> onServerErrorInternal(resp, WHEN_GETTING_TRACKING_LINKS)
                )
                .bodyToMono(AllLinksApiResponse.class).block();
    }

    @Override
    public LinkResponse addLink(long tgChatId, String link) {
        return webClient
                .post()
                .uri(baseUrl, uriBuilder -> uriBuilder.path(LINKS_URL).build())
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .bodyValue(new AddLinkRequest(link))
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        resp -> onClientErrorInternal(resp, WHEN_ADDING_LINK_TO_TRACK)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> onServerErrorInternal(resp, WHEN_ADDING_LINK_TO_TRACK)
                )
                .bodyToMono(LinkResponse.class).block();
    }

    @Override
    public LinkResponse deleteLink(long tgChatId, String link) {
        return webClient
                .method(DELETE)
                .uri(baseUrl, uriBuilder -> uriBuilder.path(LINKS_URL).build())
                .header(TG_CHAT_ID_HEADER, String.valueOf(tgChatId))
                .body(Mono.just(new RemoveLinkRequest(link)), RemoveLinkRequest.class)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        resp -> onClientErrorInternal(resp, WHEN_REMOVING_LINK_FROM_TRACKING)
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        resp -> onServerErrorInternal(resp, WHEN_REMOVING_LINK_FROM_TRACKING)
                )
                .bodyToMono(LinkResponse.class).block();
    }

    private Mono<? extends RuntimeException> onClientErrorInternal(ClientResponse resp, String when) {
        LOGGER.error("Incorrect Scrapper API request while " + when);
        return resp.bodyToMono(ApiErrorResponse.class).map(ApiClientErrorException::new);
    }

    private Mono<? extends RuntimeException> onServerErrorInternal(ClientResponse resp, String when) {
        LOGGER.error("Scrapper API server error while " + when);
        return resp.bodyToMono(ApiErrorResponse.class).map(ApiInternalServerErrorException::new);
    }
}
