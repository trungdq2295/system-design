// Package deps wires configuration, infrastructure, and layers together.
package deps

import (
	"fmt"
	"net/http"

	"github.com/trungdq2295/streaming-service/config"
	"github.com/trungdq2295/streaming-service/internal/handler/rest"
	"github.com/trungdq2295/streaming-service/internal/repo"
	streamrepo "github.com/trungdq2295/streaming-service/internal/repo/persistent/stream"
	"github.com/trungdq2295/streaming-service/internal/service"
	streamsvc "github.com/trungdq2295/streaming-service/internal/service/stream"
	"github.com/trungdq2295/streaming-service/internal/usecase"
	streamuc "github.com/trungdq2295/streaming-service/internal/usecase/stream"
	"github.com/trungdq2295/streaming-service/pkg/httpserver"
	"github.com/trungdq2295/streaming-service/pkg/logger"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

// Dependencies holds all wired components.
type Dependencies struct {
	Config *config.Config
	Logger logger.Logger

	DB *gorm.DB

	StreamRepo    repo.StreamRepo
	StreamService service.StreamService
	StreamUsecase usecase.Stream

	HTTPServer *httpserver.Server
}

// Initialize constructs all dependencies.
func Initialize(cfg *config.Config, lg logger.Logger) (*Dependencies, error) {
	lg.Info("Initializing dependencies...")

	d := &Dependencies{Config: cfg, Logger: lg}

	if err := d.initializeDatabase(); err != nil {
		return nil, fmt.Errorf("database initialization failed: %w", err)
	}

	d.initializeRepos()
	d.initializeServices()
	d.initializeUsecases()
	d.initializeHTTPServer()

	lg.Info("Dependencies initialized successfully")
	return d, nil
}

func (d *Dependencies) initializeDatabase() error {
	cfg := d.Config.Database
	dsn := fmt.Sprintf(
		"host=%s port=%s user=%s password=%s dbname=%s sslmode=%s TimeZone=UTC",
		cfg.Host, cfg.Port, cfg.User, cfg.Password, cfg.Name, cfg.SSLMode,
	)

	db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		return fmt.Errorf("failed to connect to database: %w", err)
	}

	// Auto-migrate schema for demo convenience.
	if err := db.AutoMigrate(&streamrepo.Stream{}); err != nil {
		return fmt.Errorf("failed to migrate schema: %w", err)
	}

	d.DB = db
	d.Logger.Info("Database connection established", "host", cfg.Host, "database", cfg.Name)
	return nil
}

func (d *Dependencies) initializeRepos() {
	d.StreamRepo = streamrepo.NewStreamRepo(d.DB, d.Logger)
}

func (d *Dependencies) initializeServices() {
	d.StreamService = streamsvc.NewService(d.StreamRepo, d.Config.CDN.BaseURL, d.Logger)
}

func (d *Dependencies) initializeUsecases() {
	d.StreamUsecase = streamuc.NewUsecase(d.StreamService, d.Logger)
}

func (d *Dependencies) initializeHTTPServer() {
	mux := http.NewServeMux()

	streamHandler := rest.NewStreamHandler(d.StreamUsecase, d.Logger)
	healthHandler := rest.NewHealthHandler(d.Logger)

	mux.HandleFunc("POST /api/v1/streams", streamHandler.CreateStream)
	mux.HandleFunc("GET /api/v1/streams/{id}", streamHandler.GetStream)
	mux.HandleFunc("POST /api/v1/streams/{id}/start", streamHandler.StartStream)
	mux.HandleFunc("POST /api/v1/streams/{id}/stop", streamHandler.StopStream)
	mux.HandleFunc("GET /healthz", healthHandler.Check)

	d.HTTPServer = httpserver.New(
		d.Logger,
		httpserver.Port(d.Config.HTTP.Port),
		httpserver.Handler(mux),
	)
}

// CleanUp releases resources (e.g. DB connection pool).
func (d *Dependencies) CleanUp() error {
	d.Logger.Info("Cleaning up dependencies...")
	if d.DB != nil {
		if sqlDB, err := d.DB.DB(); err == nil {
			_ = sqlDB.Close()
		}
	}
	d.Logger.Info("Cleanup completed")
	return nil
}
